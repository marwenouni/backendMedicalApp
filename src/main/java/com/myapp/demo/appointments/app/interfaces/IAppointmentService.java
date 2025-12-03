package com.myapp.demo.appointments.app.interfaces;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myapp.demo.appointments.api.dto.AppointmentCreateUpdateDto;
import com.myapp.demo.appointments.api.dto.AppointmentDto;

public interface IAppointmentService {

	// CRUD operations
	AppointmentDto getAppointmentById(Long id);

	AppointmentDto add(AppointmentCreateUpdateDto dto);

	AppointmentDto update(Long id, AppointmentCreateUpdateDto dto);

	void delete(Long id);

	// UUID lookup
	Optional<AppointmentDto> findByAppointmentUuid(String appointmentUuid);

	// Patient queries
	List<AppointmentDto> findByPatient(Long patientId);

	Page<AppointmentDto> findByPatientPaged(Long patientId, Pageable pageable);

	List<AppointmentDto> findByPatientAndStatus(Long patientId, String status);

	Page<AppointmentDto> findByPatientAndStatusPaged(Long patientId, String status, Pageable pageable);

	List<AppointmentDto> findByPatientInDateRange(Long patientId, LocalDateTime start, LocalDateTime end);

	Page<AppointmentDto> findByPatientInDateRangePaged(Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable);

	// Provider queries
	List<AppointmentDto> findByProvider(Long providerId);

	Page<AppointmentDto> findByProviderPaged(Long providerId, Pageable pageable);

	List<AppointmentDto> findByProviderAndStatus(Long providerId, String status);

	Page<AppointmentDto> findByProviderAndStatusPaged(Long providerId, String status, Pageable pageable);

	List<AppointmentDto> findByProviderInDateRange(Long providerId, LocalDateTime start, LocalDateTime end);

	Page<AppointmentDto> findByProviderInDateRangePaged(Long providerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

	// Cabinet queries
	List<AppointmentDto> findByIdCabinet(Long idCabinet);

	Page<AppointmentDto> findByIdCabinetPaged(Long idCabinet, Pageable pageable);

	List<AppointmentDto> findByIdCabinetAndStatus(Long idCabinet, String status);

	Page<AppointmentDto> findByIdCabinetAndStatusPaged(Long idCabinet, String status, Pageable pageable);

	List<AppointmentDto> findByIdCabinetInDateRange(Long idCabinet, LocalDateTime start, LocalDateTime end);

	Page<AppointmentDto> findByIdCabinetInDateRangePaged(Long idCabinet, LocalDateTime start, LocalDateTime end, Pageable pageable);

	// Status queries
	List<AppointmentDto> findByStatus(String status);

	Page<AppointmentDto> findByStatusPaged(String status, Pageable pageable);

	// Date range queries
	List<AppointmentDto> findByDateRange(LocalDateTime start, LocalDateTime end);

	Page<AppointmentDto> findByDateRangePaged(LocalDateTime start, LocalDateTime end, Pageable pageable);

	// Updated since queries (for sync)
	List<AppointmentDto> findUpdatedSince(Instant since);

	Page<AppointmentDto> findUpdatedSincePaged(Instant since, Pageable pageable);

	// Updated since queries with filters
	List<AppointmentDto> findUpdatedSinceByIdCabinet(Instant since, Long idCabinet);

	Page<AppointmentDto> findUpdatedSinceByIdCabinetPaged(Instant since, Long idCabinet, Pageable pageable);

	List<AppointmentDto> findUpdatedSinceByProviderId(Instant since, Long providerId);

	Page<AppointmentDto> findUpdatedSinceByProviderIdPaged(Instant since, Long providerId, Pageable pageable);

	List<AppointmentDto> findUpdatedSinceByIdCabinetAndProviderId(Instant since, Long idCabinet, Long providerId);

	Page<AppointmentDto> findUpdatedSinceByIdCabinetAndProviderIdPaged(Instant since, Long idCabinet, Long providerId, Pageable pageable);

	// Appointment confirmation
	AppointmentDto confirmAppointment(Long id);

	AppointmentDto cancelAppointment(Long id, String cancellationReason);

	// Idempotent creation
	AppointmentDto createIdempotent(AppointmentCreateUpdateDto dto, String appointmentUuid);
}
