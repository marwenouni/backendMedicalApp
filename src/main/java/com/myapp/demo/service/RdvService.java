package com.myapp.demo.service;

import com.myapp.demo.dto.*;
import com.myapp.demo.entity.Consultation;
import com.myapp.demo.entity.RendezVous;
import com.myapp.demo.entity.Unavailability;
import com.myapp.demo.entity.WorkingHours;
import com.myapp.demo.Repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RdvService implements IRdvService {
	@Autowired
	private WorkingHoursRepository workingHoursRepo;
	@Autowired
	private UnavailabilityRepository unavailabilityRepo;
	@Autowired
	private RendezVousRepository rendezVousRepo;

	// ====== SLOTS ======
	@Override
	public List<SlotDTO> computeSlots(Long cabinetId, String dateFrom, String dateTo, String tz) {
		ZoneId zone = ZoneId.of(tz == null ? "Europe/Paris" : tz);
		LocalDate startDate = LocalDate.parse(dateFrom);
		LocalDate endDate = LocalDate.parse(dateTo);
		if (endDate.isBefore(startDate))
			return List.of();

		List<WorkingHours> hours = workingHoursRepo.findByCabinetId(cabinetId);

		ZonedDateTime zStart = startDate.atStartOfDay(zone);
		ZonedDateTime zEnd = endDate.plusDays(1).atStartOfDay(zone).minusNanos(1);

		List<Unavailability> unavs = unavailabilityRepo.findByCabinetIdAndRange(cabinetId, zStart.toInstant(),
				zEnd.toInstant());
		List<RendezVous> rdvs = rendezVousRepo.findActiveByCabinetIdAndRange(cabinetId, zStart.toInstant(),
				zEnd.toInstant());

		Map<LocalDate, List<Interval>> unavByDay = groupIntervalsByDay(unavs, zone);
		Map<LocalDate, List<Interval>> rdvByDay = groupIntervalsByDay(rdvs, zone);

		List<SlotDTO> out = new ArrayList<>();
		for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
			int weekday = d.getDayOfWeek().getValue();
			List<WorkingHours> dayHours = hours.stream().filter(h -> h.getWeekday() == weekday).toList();
			if (dayHours.isEmpty())
				continue;

			List<Interval> dayUnav = unavByDay.getOrDefault(d, List.of());
			List<Interval> dayRdvs = rdvByDay.getOrDefault(d, List.of());

			for (WorkingHours wh : dayHours) {
				int slotMin = (wh.getSlotMinutes() != null && wh.getSlotMinutes() > 0) ? wh.getSlotMinutes() : 15;
				ZonedDateTime winStart = ZonedDateTime.of(d, wh.getStart(), zone);
				ZonedDateTime winEnd = ZonedDateTime.of(d, wh.getEnd(), zone);
				if (!winEnd.isAfter(winStart))
					continue;

				for (ZonedDateTime s = winStart; s.isBefore(winEnd); s = s.plusMinutes(slotMin)) {
					ZonedDateTime e = s.plusMinutes(slotMin);
					if (e.isAfter(winEnd))
						break;

					Interval slot = new Interval(s.toInstant(), e.toInstant());
					boolean overlapsUnav = overlapsAny(slot, dayUnav);
					boolean overlapsRdv = overlapsAny(slot, dayRdvs);

					out.add(new SlotDTO(d.format(DateTimeFormatter.ISO_LOCAL_DATE), slot.start(), slot.end(),
							!(overlapsUnav || overlapsRdv)));
				}
			}
		}
		return out;
	}

	// ====== BOOK ======
	@Override
	@Transactional
	public RdvDTO book(BookDTO dto) {
		if (dto.getCabinetId() == null || dto.getPatientId() == null || dto.getStartAt() == null
				|| dto.getEndAt() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing fields");
		}

		// idempotence
		if (dto.getClientUuid() != null) {
			Optional<RendezVous> existing = rendezVousRepo.findByClientUuid(dto.getClientUuid());
			if (existing.isPresent())
				return RdvDTO.of(existing.get());
		}

		Instant s = parseInstant(dto.getStartAt());
		Instant e = parseInstant(dto.getEndAt());
		if (!e.isAfter(s))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endAt <= startAt");

		// anti-chevauchement
		boolean conflict = rendezVousRepo.findActiveByCabinetIdAndRange(dto.getCabinetId(), s, e).stream()
				.anyMatch(r -> overlaps(s, e, r.getStartAt(), r.getEndAt()));
		if (conflict)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot already booked");

		RendezVous r = new RendezVous();
		r.setCabinetId(dto.getCabinetId());
		r.setPatientId(dto.getPatientId());
		r.setStartAt(s);
		r.setEndAt(e);
		r.setStatus(RendezVous.Status.BOOKED);
		r.setMotif(dto.getMotif());
		r.setClientUuid(dto.getClientUuid());
		RendezVous saved = rendezVousRepo.save(r);

		return RdvDTO.of(saved);
	}

	// ====== CANCEL ======
	@Override
	@Transactional
	public RdvDTO cancel(Long id, String reason) {
		RendezVous r = rendezVousRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RDV not found"));
		r.setStatus(RendezVous.Status.CANCELLED);
		if (reason != null && !reason.isBlank()) {
			r.setMotif((r.getMotif() == null ? "" : r.getMotif() + " | ") + "[CANCEL] " + reason);
		}
		return RdvDTO.of(r);
	}

	// ====== MOVE ======
	@Override
	@Transactional
	public RdvDTO move(Long id, MoveDTO dto) {
		RendezVous r = rendezVousRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RDV not found"));

		Instant ns = parseInstant(dto.getNewStartAt());
		Instant ne = parseInstant(dto.getNewEndAt());
		if (!ne.isAfter(ns))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "newEndAt <= newStartAt");

		boolean conflict = rendezVousRepo.findActiveByCabinetIdAndRange(r.getCabinetId(), ns, ne).stream()
				.anyMatch(x -> !Objects.equals(x.getId(), r.getId()) && overlaps(ns, ne, x.getStartAt(), x.getEndAt()));
		if (conflict)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Target slot already booked");

		r.setMovedFromId(r.getId());
		r.setStartAt(ns);
		r.setEndAt(ne);
		return RdvDTO.of(r);
	}

	// ====== LIST ======
	@Override
	@Transactional(readOnly = true)
	public List<RdvDTO> list(Long cabinetId, String dateFrom, String dateTo) {
		Instant s = LocalDate.parse(dateFrom).atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
		Instant e = LocalDate.parse(dateTo).plusDays(1).atStartOfDay(ZoneId.of("Europe/Paris")).minusNanos(1)
				.toInstant();
		return rendezVousRepo.findAllByCabinetIdAndRange(cabinetId, s, e).stream().map(RdvDTO::of).toList();
	}

	// ===== utils =====
	private record Interval(Instant start, Instant end) {
	}

	private static boolean overlap(Interval a, Interval b) {
		return !(a.end().isBefore(b.start()) || b.end().isBefore(a.start()));
	}

	private static boolean overlapsAny(Interval s, List<Interval> list) {
		for (Interval i : list)
			if (overlap(s, i))
				return true;
		return false;
	}

	private static boolean overlaps(Instant aS, Instant aE, Instant bS, Instant bE) {
		return !(aE.isBefore(bS) || bE.isBefore(aS));
	}

	private static Map<LocalDate, List<Interval>> groupIntervalsByDay(List<?> items, ZoneId zone) {
		Map<LocalDate, List<Interval>> map = new HashMap<>();
		for (Object o : items) {
			Instant a, b;
			RendezVous.Status st = null;
			if (o instanceof Unavailability u) {
				a = u.getStartAt();
				b = u.getEndAt();
			} else if (o instanceof com.myapp.demo.entity.RendezVous r) {
				a = r.getStartAt();
				b = r.getEndAt();
				st = r.getStatus();
			} else
				continue;
			if (st != null && st != RendezVous.Status.BOOKED)
				continue;
			LocalDate day = LocalDateTime.ofInstant(a, zone).toLocalDate();
			map.computeIfAbsent(day, __ -> new ArrayList<>()).add(new Interval(a, b));
		}
		return map;
	}

	private static Instant parseInstant(String iso) {
		try {
			return Instant.parse(iso);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ISO instant: " + iso);
		}
	}

	@Override
	public Page findUpdatedSince(Instant since, Pageable pageable) {
		return rendezVousRepo.findByUpdatedAtAfterOrderByUpdatedAtAsc(since, pageable);
	}

	@Override
	public Page<RendezVous> findByUpdatedAtAfter(Instant since, Pageable paging) {
		return rendezVousRepo.findByUpdatedAtAfter(since, paging);

	}
}