package com.myapp.demo.encounters.api;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.demo.encounters.api.dto.EncounterCreateUpdateDto;
import com.myapp.demo.encounters.api.dto.EncounterDto;
import com.myapp.demo.encounters.app.interfaces.IEncounterService;

@RestController
@RequestMapping("/api/v1/encounters")
public class EncounterController {

	@Autowired
	private IEncounterService encounterService;

	// GET All Encounters
	@GetMapping
	public ResponseEntity<List<EncounterDto>> getAllEncounters() {
		List<EncounterDto> encounters = encounterService.getAllEncounters();
		return ResponseEntity.ok(encounters);
	}

	// GET Encounter by ID
	@GetMapping("/{id}")
	public ResponseEntity<EncounterDto> getEncounterById(@PathVariable Long id) {
		EncounterDto encounter = encounterService.getEncounterById(id);
		if (encounter == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(encounter);
	}

	// GET Encounter by UUID
	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<EncounterDto> getEncounterByUuid(@PathVariable String uuid) {
		Optional<EncounterDto> encounter = encounterService.findByEncounterUuid(uuid);
		return encounter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// GET Encounter by Client UUID
	@GetMapping("/encouter-uuid/{clientUuid}")
	public ResponseEntity<EncounterDto> getEncounterByClientUuid(@PathVariable String encounterUuid) {
		Optional<EncounterDto> encounter = encounterService.findByEncounterUuid(encounterUuid);
		return encounter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// POST Create Encounter
	@PostMapping
	public ResponseEntity<EncounterDto> createEncounter(@RequestBody EncounterCreateUpdateDto dto) {
		EncounterDto created = encounterService.add(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// POST Create Encounter (Idempotent with ClientUuid)
	@PostMapping("/add-encounter")
	public ResponseEntity<EncounterDto> addEncounter(@RequestBody EncounterCreateUpdateDto dto) {
		// 1) Si déjà existant → OK idempotent
		if (dto.encouterUuid() != null && !dto.encouterUuid().isBlank()) {
			var existing = encounterService.findByEncounterUuid(dto.encouterUuid());
			if (existing.isPresent()) {
				var e = existing.get();
				return ResponseEntity.ok(e);
			}
		}
		EncounterDto saved = encounterService.createIdempotent(dto, dto.encouterUuid());
		return ResponseEntity.created(java.net.URI.create("/api/v1/encounters/" + saved.id())).body(saved);
	}

	// PUT Update Encounter
	@PutMapping("/{id}")
	public ResponseEntity<EncounterDto> updateEncounter(@PathVariable Long id, @RequestBody EncounterCreateUpdateDto dto) {
		EncounterDto updated = encounterService.update(id, dto);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	// DELETE Encounter
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEncounter(@PathVariable Long id) {
		encounterService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// GET Encounters by Patient ID
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<EncounterDto>> findByPatient(@PathVariable Long patientId) {
		List<EncounterDto> encounters = encounterService.findByPatient(patientId);
		return ResponseEntity.ok(encounters);
	}

	// GET Encounters by Patient ID with Pagination
	@GetMapping("/patient/{patientId}/paged")
	public ResponseEntity<Page<EncounterDto>> findByPatientPaged(
		@PathVariable Long patientId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<EncounterDto> encounters = encounterService.findByPatientPaged(patientId, pageable);
		return ResponseEntity.ok(encounters);
	}

	// GET Encounters by Cabinet ID
	@GetMapping("/cabinet/{idCabinet}")
	public ResponseEntity<List<EncounterDto>> findByIdCabinet(@PathVariable Long idCabinet) {
		List<EncounterDto> encounters = encounterService.findByIdCabinet(idCabinet);
		return ResponseEntity.ok(encounters);
	}

	// GET Encounters by Cabinet ID with Pagination
	@GetMapping("/cabinet/{idCabinet}/paged")
	public ResponseEntity<Page<EncounterDto>> findByIdCabinetPaged(
		@PathVariable Long idCabinet,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<EncounterDto> encounters = encounterService.findByIdCabinetPaged(idCabinet, pageable);
		return ResponseEntity.ok(encounters);
	}

	// GET Encounters updated since a timestamp
	@GetMapping("/updated-since")
	public ResponseEntity<List<EncounterDto>> findUpdatedSince(
		@RequestParam Long since,
		@RequestParam(required = false) Long idCabinet,
		@RequestParam(required = false) Long idProvider
	) {
		Instant sinceInstant = Instant.ofEpochMilli(since);
		List<EncounterDto> encounters;

		if (idCabinet != null && idProvider != null) {
			encounters = encounterService.findUpdatedSinceByIdCabinetAndProviderId(sinceInstant, idCabinet, idProvider);
		} else if (idCabinet != null) {
			encounters = encounterService.findUpdatedSinceByIdCabinet(sinceInstant, idCabinet);
		} else if (idProvider != null) {
			encounters = encounterService.findUpdatedSinceByProviderId(sinceInstant, idProvider);
		} else {
			encounters = encounterService.findUpdatedSince(sinceInstant);
		}

		return ResponseEntity.ok(encounters);
	}

	// GET Encounters updated after a timestamp with Pagination
	@GetMapping("/updated-after")
	public ResponseEntity<Page<EncounterDto>> findByUpdatedAtAfter(
		@RequestParam Long since,
		@RequestParam(required = false) Long idCabinet,
		@RequestParam(required = false) Long idProvider,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Instant sinceInstant = Instant.ofEpochMilli(since);
		Pageable pageable = PageRequest.of(page, size);
		Page<EncounterDto> encounters;

		if (idCabinet != null && idProvider != null) {
			encounters = encounterService.findUpdatedSinceByIdCabinetAndProviderIdPaged(sinceInstant, idCabinet, idProvider, pageable);
		} else if (idCabinet != null) {
			encounters = encounterService.findUpdatedSinceByIdCabinetPaged(sinceInstant, idCabinet, pageable);
		} else if (idProvider != null) {
			encounters = encounterService.findUpdatedSinceByProviderIdPaged(sinceInstant, idProvider, pageable);
		} else {
			encounters = encounterService.findByUpdatedAtAfter(sinceInstant, pageable);
		}

		return ResponseEntity.ok(encounters);
	}


	// GET All Encounters with Pagination
	@GetMapping("/paged")
	public ResponseEntity<Page<EncounterDto>> findAll(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<EncounterDto> encounters = encounterService.findAll(pageable);
		return ResponseEntity.ok(encounters);
	}

}
