package com.myapp.demo.patients.domain;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Index;

@Entity @Table(name="patient_core",
indexes = {
 @Index(name="idx_core_client_uuid", columnList="client_uuid", unique=true),
 @Index(name="idx_core_updated_at", columnList="updated_at")
})
@Getter @Setter
public class PatientCore {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name="id_cabinet") private Long idCabinet;
@Column(name="id_provider", nullable=false, unique=false) private Long idProvider;
@Column(name="client_uuid", nullable=false, unique=true, length=64) private String clientUuid;

@Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
@Column(name="updated_at", nullable=false) private Instant updatedAt;
@Column(name="deleted_at") private Instant deletedAt;

@Column(name="last_encounter_at") private Instant lastEncounterAt;
@Column(name="next_appointment_at") private Instant nextAppointmentAt;

@Version @Column(name="version") private Long version;

@PrePersist void prePersist(){ var now=Instant.now(); createdAt=now; updatedAt=now; }
@PreUpdate  void preUpdate(){  updatedAt=Instant.now(); }

// Agrégations 1–1
@OneToOne(mappedBy="core", cascade=CascadeType.ALL, optional=false, orphanRemoval=true)
private PatientIdentity identity;
@OneToOne(mappedBy="core", cascade=CascadeType.ALL, optional=false, orphanRemoval=true)
private PatientClinical clinical;
@OneToOne(mappedBy="core", cascade=CascadeType.ALL, optional=false, orphanRemoval=true)
private PatientInsurance insurance;
@OneToOne(mappedBy="core", cascade=CascadeType.ALL, optional=false, orphanRemoval=true)
private PatientConsent consent;

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Long getIdCabinet() {
	return idCabinet;
}
public void setIdCabinet(Long idCabinet) {
	this.idCabinet = idCabinet;
}
public String getClientUuid() {
	return clientUuid;
}
public void setClientUuid(String clientUuid) {
	this.clientUuid = clientUuid;
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
public Instant getLastEncounterAt() {
	return lastEncounterAt;
}
public void setLastEncounterAt(Instant lastEncounterAt) {
	this.lastEncounterAt = lastEncounterAt;
}
public Instant getNextAppointmentAt() {
	return nextAppointmentAt;
}
public void setNextAppointmentAt(Instant nextAppointmentAt) {
	this.nextAppointmentAt = nextAppointmentAt;
}
public Long getVersion() {
	return version;
}
public void setVersion(Long version) {
	this.version = version;
}
public PatientIdentity getIdentity() {
	return identity;
}
public void setIdentity(PatientIdentity identity) {
	this.identity = identity;
}
public PatientClinical getClinical() {
	return clinical;
}
public void setClinical(PatientClinical clinical) {
	this.clinical = clinical;
}
public PatientInsurance getInsurance() {
	return insurance;
}
public void setInsurance(PatientInsurance insurance) {
	this.insurance = insurance;
}
public PatientConsent getConsent() {
	return consent;
}
public void setConsent(PatientConsent consent) {
	this.consent = consent;
}
public Long getIdProvider() {
	return idProvider;
}
public void setIdProvider(Long idProvider) {
	this.idProvider = idProvider;
}

}

