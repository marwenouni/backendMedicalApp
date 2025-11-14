package com.myapp.demo.controller;

import com.myapp.demo.Repository.IConsultationRepository;
import com.myapp.demo.Repository.IDocumentRepository;
import com.myapp.demo.Repository.RendezVousRepository;
import com.myapp.demo.dto.*;
import com.myapp.demo.entity.Consultation;
import com.myapp.demo.entity.RendezVous;
import com.myapp.demo.service.IRdvService;
import com.myapp.demo.service.RdvService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rdv")
@RequiredArgsConstructor
public class RdvController {

	@Autowired
	private IRdvService rdvService;
	
	
	

	@GetMapping("/slots")
	public Map<String, Object> slots(@RequestParam Long cabinetId, @RequestParam String dateFrom, // "YYYY-MM-DD"
			@RequestParam String dateTo, // "YYYY-MM-DD"
			@RequestParam(defaultValue = "Europe/Paris") String tz) {
		List<SlotDTO> slots = rdvService.computeSlots(cabinetId, dateFrom, dateTo, tz);
		return Map.of("slots", slots);
	}

	@PostMapping("/book")
	public Map<String, Object> book(@RequestBody BookDTO dto) {
		return Map.of("rdv", rdvService.book(dto));
	}

	@PostMapping("/{id}/cancel")
	public Map<String, Object> cancel(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
		return Map.of("rdv", rdvService.cancel(id, body != null ? body.get("reason") : null));
	}

	@PostMapping("/{id}/move")
	public Map<String, Object> move(@PathVariable Long id, @RequestBody MoveDTO dto) {
		return Map.of("rdv", rdvService.move(id, dto));
	}

	@GetMapping
	public Map<String, Object> list(@RequestParam Long cabinetId, @RequestParam String dateFrom,
			@RequestParam String dateTo) {
		return Map.of("items", rdvService.list(cabinetId, dateFrom, dateTo));
	}

	
	
	@GetMapping("/updated-since")
	public ResponseEntity<Map<String, Object>> updatedSince(@RequestParam("since") long sinceEpochMs,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) {

		Instant since = Instant.ofEpochMilli(sinceEpochMs);
		Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"));

		Page<RendezVous> pageRes = rdvService.findByUpdatedAtAfter(since, paging);

		long maxSeen = pageRes.getContent().stream()
				.map(p -> p.getUpdatedAt() != null ? p.getUpdatedAt().toEpochMilli() : sinceEpochMs)
				.max(Long::compareTo).orElse(sinceEpochMs);

		Map<String, Object> body = new HashMap<>();
		body.put("rdvs", pageRes.getContent());
		body.put("currentPage", pageRes.getNumber());
		body.put("totalPages", pageRes.getTotalPages());
		body.put("nextSince", maxSeen);

		return ResponseEntity.ok(body);
	}
	
	
}
