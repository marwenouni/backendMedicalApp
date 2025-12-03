package com.myapp.demo.appointments.app;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.myapp.demo.appointments.api.dto.AppointmentCreateUpdateDto;
import com.myapp.demo.appointments.api.dto.AppointmentDto;
import com.myapp.demo.appointments.app.interfaces.IAppointmentService;
import com.myapp.demo.appointments.domain.AppointmentCore;
import com.myapp.demo.appointments.infra.repository.AppointmentRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Override
	public AppointmentDto getAppointmentById(Long id) {
		return appointmentRepository.findById(id)
			.map(this::toDto)
			.orElse(null);
	}

	@Override
	@Transactional
	public AppointmentDto add(AppointmentCreateUpdateDto dto) {
		AppointmentCore core = createCoreFromDto(dto);
		core.setAppointmentUuid(UUID.randomUUID().toString());
		AppointmentCore saved = appointmentRepository.save(core);
		return toDto(saved);
	}

	@Override
	@Transactional
	public AppointmentDto update(Long id, AppointmentCreateUpdateDto dto) {
		return appointmentRepository.findById(id)
			.map(core -> {
				updateCoreFromDto(core, dto);
				AppointmentCore updated = appointmentRepository.save(core);
				return toDto(updated);
			})
			.orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		appointmentRepository.findById(id).ifPresent(core -> {
			core.setDeletedAt(Instant.now());
			appointmentRepository.save(core);
		});
	}

	@Override
	public Optional<AppointmentDto> findByAppointmentUuid(String appointmentUuid) {
		return appointmentRepository.findByAppointmentUuid(appointmentUuid)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByPatient(Long patientId) {
		return appointmentRepository.findByPatientId(patientId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByPatientPaged(Long patientId, Pageable pageable) {
		return appointmentRepository.findByPatientIdOrderByStartTimeDesc(patientId, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByPatientAndStatus(Long patientId, String status) {
		return appointmentRepository.findByPatientIdAndStatus(patientId, status).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByPatientAndStatusPaged(Long patientId, String status, Pageable pageable) {
		return appointmentRepository.findByPatientIdAndStatusOrderByStartTimeDesc(patientId, status, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByPatientInDateRange(Long patientId, LocalDateTime start, LocalDateTime end) {
		return appointmentRepository.findByPatientIdAndStartTimeBetweenOrderByStartTimeAsc(patientId, start, end).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByPatientInDateRangePaged(Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
		return appointmentRepository.findByPatientIdAndStartTimeBetweenOrderByStartTimeAsc(patientId, start, end, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByProvider(Long providerId) {
		return appointmentRepository.findByProviderId(providerId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByProviderPaged(Long providerId, Pageable pageable) {
		return appointmentRepository.findByProviderIdOrderByStartTimeDesc(providerId, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByProviderAndStatus(Long providerId, String status) {
		return appointmentRepository.findByProviderIdAndStatus(providerId, status).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByProviderAndStatusPaged(Long providerId, String status, Pageable pageable) {
		return appointmentRepository.findByProviderIdAndStatusOrderByStartTimeDesc(providerId, status, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByProviderInDateRange(Long providerId, LocalDateTime start, LocalDateTime end) {
		return appointmentRepository.findByProviderIdAndStartTimeBetweenOrderByStartTimeAsc(providerId, start, end).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByProviderInDateRangePaged(Long providerId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
		return appointmentRepository.findByProviderIdAndStartTimeBetweenOrderByStartTimeAsc(providerId, start, end, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByIdCabinet(Long idCabinet) {
		return appointmentRepository.findByIdCabinet(idCabinet).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByIdCabinetPaged(Long idCabinet, Pageable pageable) {
		return appointmentRepository.findByIdCabinetOrderByStartTimeDesc(idCabinet, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByIdCabinetAndStatus(Long idCabinet, String status) {
		return appointmentRepository.findByIdCabinetAndStatus(idCabinet, status).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByIdCabinetAndStatusPaged(Long idCabinet, String status, Pageable pageable) {
		return appointmentRepository.findByIdCabinetAndStatusOrderByStartTimeDesc(idCabinet, status, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByIdCabinetInDateRange(Long idCabinet, LocalDateTime start, LocalDateTime end) {
		return appointmentRepository.findByIdCabinetAndStartTimeBetweenOrderByStartTimeAsc(idCabinet, start, end).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByIdCabinetInDateRangePaged(Long idCabinet, LocalDateTime start, LocalDateTime end, Pageable pageable) {
		return appointmentRepository.findByIdCabinetAndStartTimeBetweenOrderByStartTimeAsc(idCabinet, start, end, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByStatus(String status) {
		return appointmentRepository.findByStatus(status).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByStatusPaged(String status, Pageable pageable) {
		return appointmentRepository.findByStatusOrderByStartTimeDesc(status, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findByDateRange(LocalDateTime start, LocalDateTime end) {
		return appointmentRepository.findByStartTimeBetweenOrderByStartTimeAsc(start, end).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findByDateRangePaged(LocalDateTime start, LocalDateTime end, Pageable pageable) {
		return appointmentRepository.findByStartTimeBetweenOrderByStartTimeAsc(start, end, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findUpdatedSince(Instant since) {
		return appointmentRepository.findByUpdatedAtAfterOrderByUpdatedAtAsc(since).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findUpdatedSincePaged(Instant since, Pageable pageable) {
		return appointmentRepository.findByUpdatedAtAfterOrderByUpdatedAtAsc(since, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findUpdatedSinceByIdCabinet(Instant since, Long idCabinet) {
		return appointmentRepository.findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(since, idCabinet).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findUpdatedSinceByIdCabinetPaged(Instant since, Long idCabinet, Pageable pageable) {
		return appointmentRepository.findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(since, idCabinet, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findUpdatedSinceByProviderId(Instant since, Long providerId) {
		return appointmentRepository.findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(since, providerId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findUpdatedSinceByProviderIdPaged(Instant since, Long providerId, Pageable pageable) {
		return appointmentRepository.findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(since, providerId, pageable)
			.map(this::toDto);
	}

	@Override
	public List<AppointmentDto> findUpdatedSinceByIdCabinetAndProviderId(Instant since, Long idCabinet, Long providerId) {
		return appointmentRepository.findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(since, idCabinet, providerId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<AppointmentDto> findUpdatedSinceByIdCabinetAndProviderIdPaged(Instant since, Long idCabinet, Long providerId, Pageable pageable) {
		return appointmentRepository.findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(since, idCabinet, providerId, pageable)
			.map(this::toDto);
	}

	@Override
	@Transactional
	public AppointmentDto confirmAppointment(Long id) {
		return appointmentRepository.findById(id)
			.map(core -> {
				core.setIsConfirmed(true);
				core.setConfirmedAt(Instant.now());
				AppointmentCore updated = appointmentRepository.save(core);
				return toDto(updated);
			})
			.orElse(null);
	}

	@Override
	@Transactional
	public AppointmentDto cancelAppointment(Long id, String cancellationReason) {
		return appointmentRepository.findById(id)
			.map(core -> {
				core.setStatus("Cancelled");
				core.setCancelledAt(Instant.now());
				core.setCancellationReason(cancellationReason);
				AppointmentCore updated = appointmentRepository.save(core);
				return toDto(updated);
			})
			.orElse(null);
	}

	@Override
	@Transactional
	public AppointmentDto createIdempotent(AppointmentCreateUpdateDto dto, String appointmentUuid) {
		if (appointmentUuid != null && !appointmentUuid.isBlank()) {
			Optional<AppointmentCore> existing = appointmentRepository.findByAppointmentUuid(appointmentUuid);
			if (existing.isPresent()) {
				return toDto(existing.get());
			}
		}
		AppointmentCore core = createCoreFromDto(dto);
		core.setAppointmentUuid(appointmentUuid != null ? appointmentUuid : UUID.randomUUID().toString());
		AppointmentCore saved = appointmentRepository.save(core);
		return toDto(saved);
	}

	private AppointmentCore createCoreFromDto(AppointmentCreateUpdateDto dto) {
		AppointmentCore core = new AppointmentCore();
		core.setAppointmentUuid(dto.appointmentUuid());
		core.setPatientId(dto.patientId());
		core.setProviderId(dto.providerId());
		core.setIdCabinet(dto.idCabinet());
		core.setFacilityId(dto.facilityId());
		core.setAppointmentType(dto.appointmentType());
		core.setStatus(dto.status());
		core.setReason(dto.reason());
		core.setNotes(dto.notes());
		core.setStartTime(dto.startTime());
		core.setEndTime(dto.endTime());
		core.setDurationMinutes(dto.durationMinutes());
		core.setLocation(dto.location());
		core.setPrimaryPayer(dto.primaryPayer());
		core.setInsurancePlan(dto.insurancePlan());
		core.setMemberId(dto.memberId());
		core.setIsConfirmed(dto.isConfirmed() != null ? dto.isConfirmed() : false);
		core.setCancellationReason(dto.cancellationReason());
		core.setVirtualMeetingLink(dto.virtualMeetingLink());
		return core;
	}

	private void updateCoreFromDto(AppointmentCore core, AppointmentCreateUpdateDto dto) {
		core.setAppointmentUuid(dto.appointmentUuid());
		core.setPatientId(dto.patientId());
		core.setProviderId(dto.providerId());
		core.setIdCabinet(dto.idCabinet());
		core.setFacilityId(dto.facilityId());
		core.setAppointmentType(dto.appointmentType());
		core.setStatus(dto.status());
		core.setReason(dto.reason());
		core.setNotes(dto.notes());
		core.setStartTime(dto.startTime());
		core.setEndTime(dto.endTime());
		core.setDurationMinutes(dto.durationMinutes());
		core.setLocation(dto.location());
		core.setPrimaryPayer(dto.primaryPayer());
		core.setInsurancePlan(dto.insurancePlan());
		core.setMemberId(dto.memberId());
		if (dto.isConfirmed() != null) {
			core.setIsConfirmed(dto.isConfirmed());
		}
		if (dto.cancellationReason() != null) {
			core.setCancellationReason(dto.cancellationReason());
		}
		core.setVirtualMeetingLink(dto.virtualMeetingLink());
	}

	private AppointmentDto toDto(AppointmentCore core) {
		if (core == null) {
			return null;
		}
		return new AppointmentDto(
			core.getId(),
			core.getAppointmentUuid(),
			core.getPatientId(),
			core.getProviderId(),
			core.getIdCabinet(),
			core.getFacilityId(),
			core.getAppointmentType(),
			core.getStatus(),
			core.getReason(),
			core.getNotes(),
			core.getStartTime(),
			core.getEndTime(),
			core.getDurationMinutes(),
			core.getLocation(),
			core.getPrimaryPayer(),
			core.getInsurancePlan(),
			core.getMemberId(),
			core.getIsConfirmed(),
			core.getConfirmedAt(),
			core.getReminderSentAt(),
			core.getCancelledAt(),
			core.getCancellationReason(),
			core.getVirtualMeetingLink(),
			core.getCreatedAt(),
			core.getUpdatedAt(),
			core.getVersion()
		);
	}
}
