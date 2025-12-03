package com.myapp.demo.appointments.infra.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.demo.appointments.domain.AppointmentCore;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentCore, Long> {

	Optional<AppointmentCore> findByAppointmentUuid(String appointmentUuid);

	List<AppointmentCore> findByPatientId(Long patientId);

	Page<AppointmentCore> findByPatientIdOrderByStartTimeDesc(Long patientId, Pageable pageable);

	List<AppointmentCore> findByProviderId(Long providerId);

	Page<AppointmentCore> findByProviderIdOrderByStartTimeDesc(Long providerId, Pageable pageable);

	List<AppointmentCore> findByIdCabinet(Long idCabinet);

	Page<AppointmentCore> findByIdCabinetOrderByStartTimeDesc(Long idCabinet, Pageable pageable);

	List<AppointmentCore> findByStatus(String status);

	Page<AppointmentCore> findByStatusOrderByStartTimeDesc(String status, Pageable pageable);

	List<AppointmentCore> findByPatientIdAndStatus(Long patientId, String status);

	Page<AppointmentCore> findByPatientIdAndStatusOrderByStartTimeDesc(Long patientId, String status, Pageable pageable);

	List<AppointmentCore> findByProviderIdAndStatus(Long providerId, String status);

	Page<AppointmentCore> findByProviderIdAndStatusOrderByStartTimeDesc(Long providerId, String status, Pageable pageable);

	List<AppointmentCore> findByIdCabinetAndStatus(Long idCabinet, String status);

	Page<AppointmentCore> findByIdCabinetAndStatusOrderByStartTimeDesc(Long idCabinet, String status, Pageable pageable);

	// Date range queries
	List<AppointmentCore> findByStartTimeBetweenOrderByStartTimeAsc(LocalDateTime start, LocalDateTime end);

	Page<AppointmentCore> findByStartTimeBetweenOrderByStartTimeAsc(LocalDateTime start, LocalDateTime end, Pageable pageable);

	List<AppointmentCore> findByPatientIdAndStartTimeBetweenOrderByStartTimeAsc(Long patientId, LocalDateTime start, LocalDateTime end);

	Page<AppointmentCore> findByPatientIdAndStartTimeBetweenOrderByStartTimeAsc(Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable);

	List<AppointmentCore> findByProviderIdAndStartTimeBetweenOrderByStartTimeAsc(Long providerId, LocalDateTime start, LocalDateTime end);

	Page<AppointmentCore> findByProviderIdAndStartTimeBetweenOrderByStartTimeAsc(Long providerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

	List<AppointmentCore> findByIdCabinetAndStartTimeBetweenOrderByStartTimeAsc(Long idCabinet, LocalDateTime start, LocalDateTime end);

	Page<AppointmentCore> findByIdCabinetAndStartTimeBetweenOrderByStartTimeAsc(Long idCabinet, LocalDateTime start, LocalDateTime end, Pageable pageable);

	// Updated since queries
	List<AppointmentCore> findByUpdatedAtAfterOrderByUpdatedAtAsc(Instant since);

	Page<AppointmentCore> findByUpdatedAtAfterOrderByUpdatedAtAsc(Instant since, Pageable pageable);

	// Updated since queries with cabinet filter
	List<AppointmentCore> findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(Instant since, Long idCabinet);

	Page<AppointmentCore> findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(Instant since, Long idCabinet, Pageable pageable);

	// Updated since queries with provider filter
	List<AppointmentCore> findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(Instant since, Long providerId);

	Page<AppointmentCore> findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(Instant since, Long providerId, Pageable pageable);

	// Updated since queries with both cabinet and provider filters
	List<AppointmentCore> findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(Instant since, Long idCabinet, Long providerId);

	Page<AppointmentCore> findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(Instant since, Long idCabinet, Long providerId, Pageable pageable);

	// Soft delete support
	List<AppointmentCore> findByDeletedAtIsNullOrderByStartTimeDesc();

	Page<AppointmentCore> findByDeletedAtIsNullOrderByStartTimeDesc(Pageable pageable);
}
