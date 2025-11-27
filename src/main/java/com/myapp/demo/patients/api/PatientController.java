package com.myapp.demo.patients.api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.demo.patients.api.dto.PatientDto;
import com.myapp.demo.patients.app.interfaces.IPatientService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;

@RestController
@RequestMapping("api/patients")
@CrossOrigin()
public class PatientController {

	private final IPatientService patientService;

	public PatientController(IPatientService patientService) {
		this.patientService = patientService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<PatientDto> getById(@PathVariable Long id) {
		return patientService.getPatientById(id).map(p -> ResponseEntity.ok(p))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/filter")
	public ResponseEntity<Map<String, Object>> findAllPatientByFilter(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		size = Math.max(1, Math.min(size, 200)); // borne
		List<PatientDto> patients = new ArrayList<>(Arrays.asList());
		Pageable paging = PageRequest.of(page, size);

		Page<PatientDto> pagePatients = patientService.findAllPatientByFilter(paging);
		patients = pagePatients.getContent();

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("patients", patients);
		response.put("currentPage", pagePatients.getNumber());
		response.put("totalItems", pagePatients.getTotalElements());
		response.put("totalPages", pagePatients.getTotalPages());

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping(value = "/by-cabinet", produces = "application/json")
	public ResponseEntity<List<PatientDto>> findAllByCabinet(@RequestParam Long idCabinet,
			@RequestParam(defaultValue = "lastName") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		if (idCabinet == null) {
			return ResponseEntity.badRequest().build();
		}
		List<PatientDto> patients = patientService.findAllPatientByIdCabinet(idCabinet);

		return ResponseEntity.ok().header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
				.header("Pragma", "no-cache").body(patients);
	}

	@GetMapping(value = "/by-provider", produces = "application/json")
	public ResponseEntity<List<PatientDto>> findAllByProvider(@RequestParam Long idProvider,
			@RequestParam(defaultValue = "lastName") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		if (idProvider == null) {
			return ResponseEntity.badRequest().build();
		}
		List<PatientDto> patients = patientService.findAllPatientByIdProvider(idProvider);

		return ResponseEntity.ok().header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
				.header("Pragma", "no-cache").body(patients);
	}

//a revoir
	@GetMapping("/searchbyfirstname")
	public ResponseEntity<Map<String, Object>> findAllPatientByFirstName(String firstname) {
		try {
			List<PatientDto> patients = new ArrayList<>(Arrays.asList());

			List<PatientDto> pagePatients = patientService.findAllPatientByFirstName(firstname);
			patients = pagePatients;

			Map<String, Object> response = new HashMap<>();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/searchbylastname")
	public ResponseEntity<Map<String, Object>> findAllPatientByLastName(String lastname) {
		try {
			List<PatientDto> patients = new ArrayList<>(Arrays.asList());

			List<PatientDto> pagePatients = patientService.findAllPatientByLastName(lastname);
			patients = pagePatients;
			Map<String, Object> response = new HashMap<>();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/searchbybirthday")
	public ResponseEntity<Map<String, Object>> findAllPatientByBirthday(LocalDate birthday) {
		try {
			List<PatientDto> patients = new ArrayList<>(Arrays.asList());

			List<PatientDto> pagePatients = patientService.findAllPatientByBirthday(birthday);
			patients = pagePatients;

			Map<String, Object> response = new HashMap<>();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/searchbyphonenumber")
	public ResponseEntity<Map<String, Object>> findAllPatientByPhoneNumber(String phonenumber) {
		try {
			List<PatientDto> patients = new ArrayList<>(Arrays.asList());

			List<PatientDto> pagePatients = patientService.findAllPatientByPhoneMobile(phonenumber);
			patients = pagePatients;

			Map<String, Object> response = new HashMap<>();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/add-patient")
	public ResponseEntity<PatientDto> addPatient(@RequestBody PatientDto dto) throws InterruptedException {

		// 1) Si déjà existant → OK idempotent
		var existing = patientService.findByClientUuid(dto.clientUuid());
		if (existing.isPresent()) {
			var p = existing.get();
			return ResponseEntity.ok(p);
		}
		PatientDto saved = patientService.createIdempotent(dto);
		return ResponseEntity.created(java.net.URI.create("/api/patients/" + saved.id())).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePatient(@RequestBody PatientDto dto) {
		if (dto == null) {
			return ResponseEntity.badRequest()
					.body(Map.of("error", "INVALID_BODY", "message", "Body PatientDto requis"));
		}

		try {
			var updated = patientService.update(dto);
			return ResponseEntity.ok().header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
					.header("Pragma", "no-cache").body(updated);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", "PATIENT_NOT_FOUND", "message", e.getMessage()));
		} catch (OptimisticLockException | org.springframework.dao.OptimisticLockingFailureException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("error", "VERSION_CONFLICT", "message", "Ressource modifiée par ailleurs"));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", "INVALID_INPUT", "message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "SERVER_ERROR", "message", e.getMessage()));
		}
	}

	@GetMapping("/updated-since")
	public ResponseEntity<List<PatientDto>> updatedSince(@RequestParam("since") long sinceEpochMs) {

		long nonNegativeSince = Math.max(0L, sinceEpochMs);

		Instant since = Instant.ofEpochMilli(nonNegativeSince);

		List<PatientDto> patients = patientService.findUpdated(since);

		return ResponseEntity.ok(patients);
	}

	@GetMapping("/by-client-uuid/{uuid}")
	public ResponseEntity<PatientDto> getByClientUuid(@PathVariable String uuid) {
		return patientService.findByClientUuid(uuid).map(p -> ResponseEntity.ok(p))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
