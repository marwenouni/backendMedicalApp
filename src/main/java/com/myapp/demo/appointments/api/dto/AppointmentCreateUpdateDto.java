package com.myapp.demo.appointments.api.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record AppointmentCreateUpdateDto(
	String appointmentUuid,
	
	@NotNull(message = "Patient ID is required")
	Long patientId,
	
	@NotNull(message = "Provider ID is required")
	Long providerId,
	
	@NotNull(message = "Cabinet ID is required")
	Long idCabinet,
	
	Long facilityId,
	
	@NotBlank(message = "Appointment type is required")
	@Size(max = 50, message = "Appointment type cannot exceed 50 characters")
	String appointmentType,
	
	@NotBlank(message = "Status is required")
	@Size(max = 32, message = "Status cannot exceed 32 characters")
	String status,
	
	@NotBlank(message = "Appointment reason is required")
	@Size(max = 500, message = "Reason cannot exceed 500 characters")
	String reason,
	
	@Size(max = 5000, message = "Notes cannot exceed 5000 characters")
	String notes,
	
	@NotNull(message = "Start time is required")
	LocalDateTime startTime,
	
	LocalDateTime endTime,
	
	@Min(value = 1, message = "Duration must be at least 1 minute")
	Integer durationMinutes,
	
	@Size(max = 255, message = "Location cannot exceed 255 characters")
	String location,
	
	@Size(max = 255, message = "Primary payer cannot exceed 255 characters")
	String primaryPayer,
	
	@Size(max = 255, message = "Insurance plan cannot exceed 255 characters")
	String insurancePlan,
	
	@Size(max = 100, message = "Member ID cannot exceed 100 characters")
	String memberId,
	
	Boolean isConfirmed,
	
	@Size(max = 255, message = "Cancellation reason cannot exceed 255 characters")
	String cancellationReason,
	
	@Size(max = 500, message = "Virtual meeting link cannot exceed 500 characters")
	String virtualMeetingLink
) {}
