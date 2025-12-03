package com.myapp.demo.encounters.domain;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "encounter_core", indexes = {
	@Index(name = "idx_encounter_uuid", columnList = "encounter_uuid", unique = true),
	@Index(name = "idx_encounter_patient", columnList = "patient_id"),
	@Index(name = "idx_encounter_provider", columnList = "provider_id"),
	@Index(name = "idx_encounter_start_time", columnList = "start_time"),
	@Index(name = "idx_encounter_status", columnList = "status")
})
@Getter
@Setter
public class EncounterCore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "encounter_uuid", nullable = false, unique = true, length = 64)
	private String encounterUuid;


	@Column(name = "patient_id", nullable = false)
	private Long patientId;

	@Column(name = "provider_id", nullable = false)
	private Long providerId;

	@Column(name = "id_cabinet")
	private Long idCabinet;

	@Column(name = "facility_id")
	private Long facilityId;

	// Encounter type: Office Visit, Telehealth, Emergency, Inpatient, etc.
	@Column(name = "encounter_type", nullable = false, length = 32)
	private String encounterType;

	// Encounter status: Draft, Completed, Cancelled, NoShow, etc.
	@Column(name = "status", nullable = false, length = 32)
	private String status;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;

	@Column(name = "duration_minutes")
	private Integer durationMinutes;

	// Chief complaint - brief description
	@Column(name = "chief_complaint", length = 500)
	private String chiefComplaint;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@Version
	@Column(name = "version")
	private Long version;

	// Aggregations 1-1
	@OneToOne(mappedBy = "core", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
	private EncounterClinical clinical;

	@OneToOne(mappedBy = "core", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
	private EncounterAssessment assessment;

	@OneToOne(mappedBy = "core", cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
	private EncounterVitals vitals;

	@PrePersist
	void prePersist() {
		var now = Instant.now();
		createdAt = now;
		updatedAt = now;
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

	public String getEncounterUuid() {
		return encounterUuid;
	}

	public void setEncounterUuid(String encounterUuid) {
		this.encounterUuid = encounterUuid;
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

	public String getEncounterType() {
		return encounterType;
	}

	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getChiefComplaint() {
		return chiefComplaint;
	}

	public void setChiefComplaint(String chiefComplaint) {
		this.chiefComplaint = chiefComplaint;
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

	public EncounterClinical getClinical() {
		return clinical;
	}

	public void setClinical(EncounterClinical clinical) {
		this.clinical = clinical;
	}

	public EncounterAssessment getAssessment() {
		return assessment;
	}

	public void setAssessment(EncounterAssessment assessment) {
		this.assessment = assessment;
	}

	public EncounterVitals getVitals() {
		return vitals;
	}

	public void setVitals(EncounterVitals vitals) {
		this.vitals = vitals;
	}

}
