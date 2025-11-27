package com.myapp.demo.patients.domain;

import java.util.List;

import com.myapp.demo.entity.PharmacyInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="patient_clinical")
@Getter @Setter
public class PatientClinical {
@Id private Long id;
@MapsId @OneToOne @JoinColumn(name="core_id") private PatientCore core;

@Column(name="blood_type", length=8) private String bloodType;

@ElementCollection
@CollectionTable(name="patient_allergy", joinColumns=@JoinColumn(name="patient_id"))
@Column(name="allergy", length=160)
private List<String> allergies;

@ElementCollection
@CollectionTable(name="patient_chronic_condition", joinColumns=@JoinColumn(name="patient_id"))
@Column(name="condition_name", length=160)
private List<String> chronicConditions;

@ElementCollection
@CollectionTable(name="patient_medication", joinColumns=@JoinColumn(name="patient_id"))
@Column(name="medication", length=160)
private List<String> medications;

@Column(name="height_cm") private Double heightCm;
@Column(name="weight_kg") private Double weightKg;
@Column(name="smoking_status", length=32) private String smokingStatus;
@Column(name="alcohol_use", length=32)    private String alcoholUse;

// SDOH
@Column(name="occupation", length=120)      private String occupation;
@Column(name="employer", length=160)        private String employer;
@Column(name="housing_status", length=64)   private String housingStatus;
@Column(name="financial_strain", length=32) private String financialStrain;
@Column(name="transport_access", length=32) private String transportationAccess;

// Pharmacie préférée (facultatif)
@Embedded private PharmacyInfo preferredPharmacy;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public PatientCore getCore() {
	return core;
}

public void setCore(PatientCore core) {
	this.core = core;
}

public String getBloodType() {
	return bloodType;
}

public void setBloodType(String bloodType) {
	this.bloodType = bloodType;
}

public List<String> getAllergies() {
	return allergies;
}

public void setAllergies(List<String> allergies) {
	this.allergies = allergies;
}

public List<String> getChronicConditions() {
	return chronicConditions;
}

public void setChronicConditions(List<String> chronicConditions) {
	this.chronicConditions = chronicConditions;
}

public List<String> getMedications() {
	return medications;
}

public void setMedications(List<String> medications) {
	this.medications = medications;
}

public Double getHeightCm() {
	return heightCm;
}

public void setHeightCm(Double heightCm) {
	this.heightCm = heightCm;
}

public Double getWeightKg() {
	return weightKg;
}

public void setWeightKg(Double weightKg) {
	this.weightKg = weightKg;
}

public String getSmokingStatus() {
	return smokingStatus;
}

public void setSmokingStatus(String smokingStatus) {
	this.smokingStatus = smokingStatus;
}

public String getAlcoholUse() {
	return alcoholUse;
}

public void setAlcoholUse(String alcoholUse) {
	this.alcoholUse = alcoholUse;
}

public String getOccupation() {
	return occupation;
}

public void setOccupation(String occupation) {
	this.occupation = occupation;
}

public String getEmployer() {
	return employer;
}

public void setEmployer(String employer) {
	this.employer = employer;
}

public String getHousingStatus() {
	return housingStatus;
}

public void setHousingStatus(String housingStatus) {
	this.housingStatus = housingStatus;
}

public String getFinancialStrain() {
	return financialStrain;
}

public void setFinancialStrain(String financialStrain) {
	this.financialStrain = financialStrain;
}

public String getTransportationAccess() {
	return transportationAccess;
}

public void setTransportationAccess(String transportationAccess) {
	this.transportationAccess = transportationAccess;
}

public PharmacyInfo getPreferredPharmacy() {
	return preferredPharmacy;
}

public void setPreferredPharmacy(PharmacyInfo preferredPharmacy) {
	this.preferredPharmacy = preferredPharmacy;
}


}
