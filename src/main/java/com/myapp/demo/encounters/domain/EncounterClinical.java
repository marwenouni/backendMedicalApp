package com.myapp.demo.encounters.domain;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "encounter_clinical")
@Getter
@Setter
public class EncounterClinical {

	@Id
	private Long id;

	@MapsId
	@OneToOne
	@JoinColumn(name = "core_id")
	private EncounterCore core;

	// History of Present Illness
	@Column(name = "hpi", columnDefinition = "LONGTEXT")
	private String historyOfPresentIllness;

	// Review of Systems
	@Column(name = "ros", columnDefinition = "LONGTEXT")
	private String reviewOfSystems;

	// Past Medical History summary in this encounter
	@Column(name = "pmh_relevant", columnDefinition = "LONGTEXT")
	private String relevantPMH;

	// Medications reviewed
	@ElementCollection
	@CollectionTable(name = "encounter_medications_reviewed", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "medication", length = 256)
	private List<String> medicationsReviewed;

	// Physical Examination
	@Column(name = "physical_exam", columnDefinition = "LONGTEXT")
	private String physicalExamination;

	// Diagnoses with ICD-10 codes
	@ElementCollection
	@CollectionTable(name = "encounter_diagnosis", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "icd10_code", length = 10)
	private List<String> diagnosisIcd10Codes;

	// Diagnosis descriptions
	@ElementCollection
	@CollectionTable(name = "encounter_diagnosis_desc", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "diagnosis_description", length = 256)
	private List<String> diagnosisDescriptions;

	// Procedures performed with CPT codes
	@ElementCollection
	@CollectionTable(name = "encounter_procedure", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "cpt_code", length = 5)
	private List<String> procedureCptCodes;

	// Procedure descriptions
	@ElementCollection
	@CollectionTable(name = "encounter_procedure_desc", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "procedure_description", length = 256)
	private List<String> procedureDescriptions;

	// SNOMED CT codes for problems/conditions
	@ElementCollection
	@CollectionTable(name = "encounter_snomed_codes", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "snomed_code", length = 32)
	private List<String> snomedCtCodes;

	// Medications prescribed/administered in this encounter
	@ElementCollection
	@CollectionTable(name = "encounter_prescribed_meds", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "medication", length = 256)
	private List<String> prescribedMedications;

	// Lab orders placed
	@ElementCollection
	@CollectionTable(name = "encounter_lab_orders", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "lab_code", length = 32)
	private List<String> labOrderCodes;

	// Imaging orders placed
	@ElementCollection
	@CollectionTable(name = "encounter_imaging_orders", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "imaging_code", length = 32)
	private List<String> imagingOrderCodes;

	// Referrals made
	@ElementCollection
	@CollectionTable(name = "encounter_referrals", joinColumns = @JoinColumn(name = "encounter_id"))
	@Column(name = "referral_specialty", length = 64)
	private List<String> referralSpecialties;

	// Patient education provided
	@Column(name = "patient_education", columnDefinition = "LONGTEXT")
	private String patientEducation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EncounterCore getCore() {
		return core;
	}

	public void setCore(EncounterCore core) {
		this.core = core;
	}

	public String getHistoryOfPresentIllness() {
		return historyOfPresentIllness;
	}

	public void setHistoryOfPresentIllness(String historyOfPresentIllness) {
		this.historyOfPresentIllness = historyOfPresentIllness;
	}

	public String getReviewOfSystems() {
		return reviewOfSystems;
	}

	public void setReviewOfSystems(String reviewOfSystems) {
		this.reviewOfSystems = reviewOfSystems;
	}

	public String getRelevantPMH() {
		return relevantPMH;
	}

	public void setRelevantPMH(String relevantPMH) {
		this.relevantPMH = relevantPMH;
	}

	public List<String> getMedicationsReviewed() {
		return medicationsReviewed;
	}

	public void setMedicationsReviewed(List<String> medicationsReviewed) {
		this.medicationsReviewed = medicationsReviewed;
	}

	public String getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public List<String> getDiagnosisIcd10Codes() {
		return diagnosisIcd10Codes;
	}

	public void setDiagnosisIcd10Codes(List<String> diagnosisIcd10Codes) {
		this.diagnosisIcd10Codes = diagnosisIcd10Codes;
	}

	public List<String> getDiagnosisDescriptions() {
		return diagnosisDescriptions;
	}

	public void setDiagnosisDescriptions(List<String> diagnosisDescriptions) {
		this.diagnosisDescriptions = diagnosisDescriptions;
	}

	public List<String> getProcedureCptCodes() {
		return procedureCptCodes;
	}

	public void setProcedureCptCodes(List<String> procedureCptCodes) {
		this.procedureCptCodes = procedureCptCodes;
	}

	public List<String> getProcedureDescriptions() {
		return procedureDescriptions;
	}

	public void setProcedureDescriptions(List<String> procedureDescriptions) {
		this.procedureDescriptions = procedureDescriptions;
	}

	public List<String> getSnomedCtCodes() {
		return snomedCtCodes;
	}

	public void setSnomedCtCodes(List<String> snomedCtCodes) {
		this.snomedCtCodes = snomedCtCodes;
	}

	public List<String> getPrescribedMedications() {
		return prescribedMedications;
	}

	public void setPrescribedMedications(List<String> prescribedMedications) {
		this.prescribedMedications = prescribedMedications;
	}

	public List<String> getLabOrderCodes() {
		return labOrderCodes;
	}

	public void setLabOrderCodes(List<String> labOrderCodes) {
		this.labOrderCodes = labOrderCodes;
	}

	public List<String> getImagingOrderCodes() {
		return imagingOrderCodes;
	}

	public void setImagingOrderCodes(List<String> imagingOrderCodes) {
		this.imagingOrderCodes = imagingOrderCodes;
	}

	public List<String> getReferralSpecialties() {
		return referralSpecialties;
	}

	public void setReferralSpecialties(List<String> referralSpecialties) {
		this.referralSpecialties = referralSpecialties;
	}

	public String getPatientEducation() {
		return patientEducation;
	}

	public void setPatientEducation(String patientEducation) {
		this.patientEducation = patientEducation;
	}

}
