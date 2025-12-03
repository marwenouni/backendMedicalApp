package com.myapp.demo.appointments.api;

import java.time.Instant;
import java.time.LocalDateTime;
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

import com.myapp.demo.appointments.api.dto.AppointmentCreateUpdateDto;
import com.myapp.demo.appointments.api.dto.AppointmentDto;
import com.myapp.demo.appointments.app.interfaces.IAppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

	@Autowired
	private IAppointmentService appointmentService;

	// GET All Appointments with Pagination
	@GetMapping
	public ResponseEntity<Page<AppointmentDto>> getAllAppointments(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments = appointmentService.findByStatusPaged("Scheduled", pageable);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointment by ID
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
		AppointmentDto appointment = appointmentService.getAppointmentById(id);
		if (appointment == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(appointment);
	}

	// GET Appointment by UUID
	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<AppointmentDto> getAppointmentByUuid(@PathVariable String uuid) {
		Optional<AppointmentDto> appointment = appointmentService.findByAppointmentUuid(uuid);
		return appointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// POST Create Appointment
	@PostMapping
	public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentCreateUpdateDto dto) {
		AppointmentDto created = appointmentService.add(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// POST Create Appointment (Idempotent with UUID)
	@PostMapping("/add-appointment")
	public ResponseEntity<AppointmentDto> addAppointment(@Valid @RequestBody AppointmentCreateUpdateDto dto) {
		if (dto.appointmentUuid() != null && !dto.appointmentUuid().isBlank()) {
			Optional<AppointmentDto> existing = appointmentService.findByAppointmentUuid(dto.appointmentUuid());
			if (existing.isPresent()) {
				return ResponseEntity.ok(existing.get());
			}
		}
		AppointmentDto saved = appointmentService.createIdempotent(dto, dto.appointmentUuid());
		return ResponseEntity.created(java.net.URI.create("/api/v1/appointments/" + saved.id())).body(saved);
	}

	// PUT Update Appointment
	@PutMapping("/{id}")
	public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentCreateUpdateDto dto) {
		AppointmentDto updated = appointmentService.update(id, dto);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	// DELETE Appointment (Soft Delete)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
		appointmentService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// GET Appointments by Patient
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<AppointmentDto>> findByPatient(@PathVariable Long patientId) {
		List<AppointmentDto> appointments = appointmentService.findByPatient(patientId);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Patient with Pagination
	@GetMapping("/patient/{patientId}/paged")
	public ResponseEntity<Page<AppointmentDto>> findByPatientPaged(
		@PathVariable Long patientId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments = appointmentService.findByPatientPaged(patientId, pageable);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Patient and Status
	@GetMapping("/patient/{patientId}/status/{status}")
	public ResponseEntity<List<AppointmentDto>> findByPatientAndStatus(
		@PathVariable Long patientId,
		@PathVariable String status
	) {
		List<AppointmentDto> appointments = appointmentService.findByPatientAndStatus(patientId, status);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Patient and Status with Pagination
	@GetMapping("/patient/{patientId}/status/{status}/paged")
	public ResponseEntity<Page<AppointmentDto>> findByPatientAndStatusPaged(
		@PathVariable Long patientId,
		@PathVariable String status,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments = appointmentService.findByPatientAndStatusPaged(patientId, status, pageable);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Patient in Date Range
	@GetMapping("/patient/{patientId}/date-range")
	public ResponseEntity<List<AppointmentDto>> findByPatientInDateRange(
		@PathVariable Long patientId,
		@RequestParam String startTime,
		@RequestParam String endTime
	) {
		LocalDateTime start = LocalDateTime.parse(startTime);
		LocalDateTime end = LocalDateTime.parse(endTime);
		List<AppointmentDto> appointments = appointmentService.findByPatientInDateRange(patientId, start, end);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Provider
	@GetMapping("/provider/{providerId}")
	public ResponseEntity<List<AppointmentDto>> findByProvider(@PathVariable Long providerId) {
		List<AppointmentDto> appointments = appointmentService.findByProvider(providerId);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Provider with Pagination
	@GetMapping("/provider/{providerId}/paged")
	public ResponseEntity<Page<AppointmentDto>> findByProviderPaged(
		@PathVariable Long providerId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments = appointmentService.findByProviderPaged(providerId, pageable);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Provider and Status
	@GetMapping("/provider/{providerId}/status/{status}")
	public ResponseEntity<List<AppointmentDto>> findByProviderAndStatus(
		@PathVariable Long providerId,
		@PathVariable String status
	) {
		List<AppointmentDto> appointments = appointmentService.findByProviderAndStatus(providerId, status);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Cabinet
	@GetMapping("/cabinet/{idCabinet}")
	public ResponseEntity<List<AppointmentDto>> findByIdCabinet(@PathVariable Long idCabinet) {
		List<AppointmentDto> appointments = appointmentService.findByIdCabinet(idCabinet);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Cabinet with Pagination
	@GetMapping("/cabinet/{idCabinet}/paged")
	public ResponseEntity<Page<AppointmentDto>> findByIdCabinetPaged(
		@PathVariable Long idCabinet,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments = appointmentService.findByIdCabinetPaged(idCabinet, pageable);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Cabinet and Status
	@GetMapping("/cabinet/{idCabinet}/status/{status}")
	public ResponseEntity<List<AppointmentDto>> findByIdCabinetAndStatus(
		@PathVariable Long idCabinet,
		@PathVariable String status
	) {
		List<AppointmentDto> appointments = appointmentService.findByIdCabinetAndStatus(idCabinet, status);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Status
	@GetMapping("/status/{status}")
	public ResponseEntity<List<AppointmentDto>> findByStatus(@PathVariable String status) {
		List<AppointmentDto> appointments = appointmentService.findByStatus(status);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments by Status with Pagination
	@GetMapping("/status/{status}/paged")
	public ResponseEntity<Page<AppointmentDto>> findByStatusPaged(
		@PathVariable String status,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments = appointmentService.findByStatusPaged(status, pageable);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments in Date Range
	@GetMapping("/date-range")
	public ResponseEntity<List<AppointmentDto>> findByDateRange(
		@RequestParam String startTime,
		@RequestParam String endTime
	) {
		LocalDateTime start = LocalDateTime.parse(startTime);
		LocalDateTime end = LocalDateTime.parse(endTime);
		List<AppointmentDto> appointments = appointmentService.findByDateRange(start, end);
		return ResponseEntity.ok(appointments);
	}

	// GET Appointments updated since timestamp (for sync)
	@GetMapping("/updated-since")
	public ResponseEntity<List<AppointmentDto>> findUpdatedSince(
		@RequestParam Long since,
		@RequestParam(required = false) Long idCabinet,
		@RequestParam(required = false) Long idProvider
	) {
		Instant sinceInstant = Instant.ofEpochMilli(since);
		List<AppointmentDto> appointments;

		if (idCabinet != null && idProvider != null) {
			appointments = appointmentService.findUpdatedSinceByIdCabinetAndProviderId(sinceInstant, idCabinet, idProvider);
		} else if (idCabinet != null) {
			appointments = appointmentService.findUpdatedSinceByIdCabinet(sinceInstant, idCabinet);
		} else if (idProvider != null) {
			appointments = appointmentService.findUpdatedSinceByProviderId(sinceInstant, idProvider);
		} else {
			appointments = appointmentService.findUpdatedSince(sinceInstant);
		}

		return ResponseEntity.ok(appointments);
	}

	// GET Appointments updated since timestamp with Pagination
	@GetMapping("/updated-since/paged")
	public ResponseEntity<Page<AppointmentDto>> findUpdatedSincePaged(
		@RequestParam Long since,
		@RequestParam(required = false) Long idCabinet,
		@RequestParam(required = false) Long idProvider,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Instant sinceInstant = Instant.ofEpochMilli(since);
		Pageable pageable = PageRequest.of(page, size);
		Page<AppointmentDto> appointments;

		if (idCabinet != null && idProvider != null) {
			appointments = appointmentService.findUpdatedSinceByIdCabinetAndProviderIdPaged(sinceInstant, idCabinet, idProvider, pageable);
		} else if (idCabinet != null) {
			appointments = appointmentService.findUpdatedSinceByIdCabinetPaged(sinceInstant, idCabinet, pageable);
		} else if (idProvider != null) {
			appointments = appointmentService.findUpdatedSinceByProviderIdPaged(sinceInstant, idProvider, pageable);
		} else {
			appointments = appointmentService.findUpdatedSincePaged(sinceInstant, pageable);
		}

		return ResponseEntity.ok(appointments);
	}

	// POST Confirm Appointment
	@PostMapping("/{id}/confirm")
	public ResponseEntity<AppointmentDto> confirmAppointment(@PathVariable Long id) {
		AppointmentDto confirmed = appointmentService.confirmAppointment(id);
		if (confirmed == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(confirmed);
	}

	// POST Cancel Appointment
	@PostMapping("/{id}/cancel")
	public ResponseEntity<AppointmentDto> cancelAppointment(
		@PathVariable Long id,
		@RequestParam String cancellationReason
	) {
		AppointmentDto cancelled = appointmentService.cancelAppointment(id, cancellationReason);
		if (cancelled == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cancelled);
	}
}
