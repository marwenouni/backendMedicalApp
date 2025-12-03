package com.myapp.demo.appointments.domain;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointment_core", indexes = {
	@Index(name = "idx_appointment_uuid", columnList = "appointment_uuid", unique = true),
	@Index(name = "idx_appointment_patient", columnList = "patient_id"),
	@Index(name = "idx_appointment_provider", columnList = "provider_id"),
	@Index(name = "idx_appointment_cabinet", columnList = "id_cabinet"),
	@Index(name = "idx_appointment_start_time", columnList = "start_time"),
	@Index(name = "idx_appointment_status", columnList = "status"),
	@Index(name = "idx_appointment_updated_at", columnList = "updated_at")
})
@Getter
@Setter
public class AppointmentCore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "appointment_uuid", nullable = false, unique = true, length = 64)
	private String appointmentUuid;

	// Patient ID (foreign key to Patient)
	@Column(name = "patient_id", nullable = false)
	private Long patientId;

	// Provider ID (NPI or internal ID)
	@Column(name = "provider_id", nullable = false)
	private Long providerId;

	// Cabinet/Facility ID
	@Column(name = "id_cabinet", nullable = false)
	private Long idCabinet;

	// Facility ID (for multi-facility support)
	@Column(name = "facility_id")
	private Long facilityId;

	// Appointment Type: Office Visit, Telehealth, Phone, Procedure, Lab, Imaging, etc.
	@Column(name = "appointment_type", nullable = false, length = 50)
	private String appointmentType;

	// Appointment Status: Scheduled, Confirmed, In-Progress, Completed, No-Show, Cancelled, Rescheduled
	@Column(name = "status", nullable = false, length = 32)
	private String status;

	// Appointment reason/chief complaint
	@Column(name = "reason", length = 500)
	private String reason;

	// Additional notes/description
	@Column(name = "notes", columnDefinition = "LONGTEXT")
	private String notes;

	// Appointment start time
	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	// Appointment end time
	@Column(name = "end_time")
	private LocalDateTime endTime;

	// Duration in minutes
	@Column(name = "duration_minutes")
	private Integer durationMinutes;

	// Location/Room info
	@Column(name = "location", length = 255)
	private String location;

	// Primary insurance/payer (for US EHR requirements)
	@Column(name = "primary_payer", length = 255)
	private String primaryPayer;

	// Insurance plan name
	@Column(name = "insurance_plan", length = 255)
	private String insurancePlan;

	// Member ID
	@Column(name = "member_id", length = 100)
	private String memberId;

	// Whether the appointment has been confirmed by patient
	@Column(name = "is_confirmed")
	private Boolean isConfirmed;

	// Confirmation timestamp
	@Column(name = "confirmed_at")
	private Instant confirmedAt;

	// Reminder sent timestamp
	@Column(name = "reminder_sent_at")
	private Instant reminderSentAt;

	// Cancelled timestamp
	@Column(name = "cancelled_at")
	private Instant cancelledAt;

	// Cancellation reason
	@Column(name = "cancellation_reason", length = 255)
	private String cancellationReason;

	// Virtual meeting link (for telehealth appointments)
	@Column(name = "virtual_meeting_link", length = 500)
	private String virtualMeetingLink;

	// Audit trail
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@Version
	@Column(name = "version")
	private Long version;

	@PrePersist
	void prePersist() {
		var now = Instant.now();
		createdAt = now;
		updatedAt = now;
		if (isConfirmed == null) {
			isConfirmed = false;
		}
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppointmentUuid() {
		return appointmentUuid;
	}

	public void setAppointmentUuid(String appointmentUuid) {
		this.appointmentUuid = appointmentUuid;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Long getIdCabinet() {
		return idCabinet;
	}

	public void setIdCabinet(Long idCabinet) {
		this.idCabinet = idCabinet;
	}

	public Long getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Integer getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPrimaryPayer() {
		return primaryPayer;
	}

	public void setPrimaryPayer(String primaryPayer) {
		this.primaryPayer = primaryPayer;
	}

	public String getInsurancePlan() {
		return insurancePlan;
	}

	public void setInsurancePlan(String insurancePlan) {
		this.insurancePlan = insurancePlan;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Boolean getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(Boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public Instant getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(Instant confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public Instant getReminderSentAt() {
		return reminderSentAt;
	}

	public void setReminderSentAt(Instant reminderSentAt) {
		this.reminderSentAt = reminderSentAt;
	}

	public Instant getCancelledAt() {
		return cancelledAt;
	}

	public void setCancelledAt(Instant cancelledAt) {
		this.cancelledAt = cancelledAt;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getVirtualMeetingLink() {
		return virtualMeetingLink;
	}

	public void setVirtualMeetingLink(String virtualMeetingLink) {
		this.virtualMeetingLink = virtualMeetingLink;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
}
