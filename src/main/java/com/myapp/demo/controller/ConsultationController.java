package com.myapp.demo.controller;

import com.myapp.demo.Repository.IConsultationRepository;
import com.myapp.demo.Repository.IDocumentRepository;
import com.myapp.demo.Repository.IChartRepository;
import com.myapp.demo.entity.Consultation;
import com.myapp.demo.entity.Chart;
import com.myapp.demo.service.IConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("api/consultation")
@CrossOrigin
public class ConsultationController {

	@Autowired
	public IConsultationService consultationService;

	// --- GET by id ---
	@GetMapping
	public ResponseEntity<Map<String, Object>> getById(@RequestParam Integer id) {
		var c = consultationService.getConsultationById(id);
		if (c == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(Map.of("consultations", c));
	}

	// --- GET all (attention: à paginer en prod) ---
	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> getAll() {
		List<Consultation> list = consultationService.getAllConsultations();
		return ResponseEntity.ok(Map.of("consultations", list));
	}

	// --- GET by chart (paginé) ---
	@GetMapping("/by-chart")
	public ResponseEntity<Map<String, Object>> byChart(@RequestParam Integer idChart,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
		Page<Consultation> res = consultationService.findByChartPaged(idChart, paging);

		Map<String, Object> body = new HashMap<>();
		body.put("consultations", res.getContent());
		body.put("currentPage", res.getNumber());
		body.put("totalItems", res.getTotalElements());
		body.put("totalPages", res.getTotalPages());
		return ResponseEntity.ok(body);
	}

	// --- GET incremental (updatedSince) ---

	@GetMapping("/updated-since")
	public ResponseEntity<Map<String, Object>> updatedSince(@RequestParam("since") long sinceEpochMs,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) {

		Instant since = Instant.ofEpochMilli(sinceEpochMs);
		Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"));

		Page<Consultation> pageRes = consultationService.findByUpdatedAtAfter(since, paging);

		long maxSeen = pageRes.getContent().stream()
				.map(p -> p.getUpdatedAt() != null ? p.getUpdatedAt().toEpochMilli() : sinceEpochMs)
				.max(Long::compareTo).orElse(sinceEpochMs);

		Map<String, Object> body = new HashMap<>();
		body.put("consultations", pageRes.getContent());
		body.put("currentPage", pageRes.getNumber());
		body.put("totalPages", pageRes.getTotalPages());
		body.put("nextSince", maxSeen);
System.out.println("execution api");
		return ResponseEntity.ok(body);
	}

	@PostMapping("/add-consultation")
	public ResponseEntity<Map<String, Object>> add(@RequestBody Consultation c) {

		if (c.getClientUuid() != null && !c.getClientUuid().isBlank()) {
			var existing = consultationService.findByClientUuid(c.getClientUuid());
			if (existing.isPresent()) {
				var e = existing.get();
				return ResponseEntity
						.ok(Map.of("consultations", Map.of("id", e.getId(), "clientUuid", e.getClientUuid())));
			}
		}
		Consultation saved = consultationService.add(c);
		return ResponseEntity
				.ok(Map.of("consultations", Map.of("id", saved.getId(), "clientUuid", saved.getClientUuid())));
	}

	// --- PUT update ---
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody Consultation c) {
		var current = consultationService.getConsultationById(id);
		if (current == null)
			return ResponseEntity.notFound().build();

		// merge minimum
		current.setObservation(c.getObservation());
		current.setTraitement(c.getTraitement());
		current.setExamen(c.getExamen());
		current.setDate(c.getDate());
		current.setIdCabinet(c.getIdCabinet());
		current.setIdChart(c.getIdChart());

		Consultation saved = consultationService.update(current);
		return ResponseEntity.ok(Map.of("consultations", saved));
	}

	// --- DELETE ---
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		var current = consultationService.getConsultationById(id);
		if (current == null)
			return ResponseEntity.notFound().build();
		consultationService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// (Optionnel) lookup idempotence
	@GetMapping("/by-client-uuid/{uuid}")
	public ResponseEntity<Map<String, Object>> byClientUuid(@PathVariable String uuid) {

		return consultationService.findByClientUuid(uuid).map(p -> {
			Map<String, Object> consultation = new HashMap<>();
			consultation.put("id", p.getId());
			consultation.put("clientUuid", p.getClientUuid());

			Map<String, Object> body = new HashMap<>();
			body.put("consultation", consultation);
			return ResponseEntity.ok(body);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/filter")
	public ResponseEntity<Map<String, Object>> filter(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		var paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
		var p = consultationService.findAll(paging);
		Map<String, Object> response = new HashMap<>();
		response.put("consultations", p.getContent());
		response.put("currentPage", p.getNumber());
		response.put("totalPages", p.getTotalPages());
		return ResponseEntity.ok(response);
	}

}
