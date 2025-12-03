package com.myapp.demo.appointments.api.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record AppointmentDto(
	Long id,
	String appointmentUuid,
	Long patientId,
	Long providerId,
	Long idCabinet,
	Long facilityId,
	String appointmentType,
	String status,
	String reason,
	String notes,
	LocalDateTime startTime,
	LocalDateTime endTime,
	Integer durationMinutes,
	String location,
	String primaryPayer,
	String insurancePlan,
	String memberId,
	Boolean isConfirmed,
	Instant confirmedAt,
	Instant reminderSentAt,
	Instant cancelledAt,
	String cancellationReason,
	String virtualMeetingLink,
	Instant createdAt,
	Instant updatedAt,
	Long version
) {}
