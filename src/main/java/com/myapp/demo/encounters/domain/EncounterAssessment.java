package com.myapp.demo.encounters.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "encounter_assessment")
@Getter
@Setter
public class EncounterAssessment {

	@Id
	private Long id;

	@MapsId
	@OneToOne
	@JoinColumn(name = "core_id")
	private EncounterCore core;

	// Assessment section - clinical impression and reasoning
	@Column(name = "assessment", columnDefinition = "LONGTEXT")
	private String assessment;

	// Plan section - treatment and management plan
	@Column(name = "plan", columnDefinition = "LONGTEXT")
	private String plan;

	// Follow-up plan
	@Column(name = "followup_plan", columnDefinition = "LONGTEXT")
	private String followupPlan;

	// Follow-up timeframe (e.g., "2 weeks", "As needed")
	@Column(name = "followup_timeframe", length = 100)
	private String followupTimeframe;

	// Prognosis
	@Column(name = "prognosis", length = 256)
	private String prognosis;

	// Goals of care discussed
	@Column(name = "goals_of_care", columnDefinition = "LONGTEXT")
	private String goalsOfCare;

	// Clinical decision making / reasoning
	@Column(name = "clinical_reasoning", columnDefinition = "LONGTEXT")
	private String clinicalReasoning;

	// Disposition (Discharged, Admitted, Referred, etc.)
	@Column(name = "disposition", length = 64)
	private String disposition;

	// Provider name and signature indicator
	@Column(name = "provider_name", length = 120)
	private String providerName;

	@Column(name = "is_signed", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isSigned;

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

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getFollowupPlan() {
		return followupPlan;
	}

	public void setFollowupPlan(String followupPlan) {
		this.followupPlan = followupPlan;
	}

	public String getFollowupTimeframe() {
		return followupTimeframe;
	}

	public void setFollowupTimeframe(String followupTimeframe) {
		this.followupTimeframe = followupTimeframe;
	}

	public String getPrognosis() {
		return prognosis;
	}

	public void setPrognosis(String prognosis) {
		this.prognosis = prognosis;
	}

	public String getGoalsOfCare() {
		return goalsOfCare;
	}

	public void setGoalsOfCare(String goalsOfCare) {
		this.goalsOfCare = goalsOfCare;
	}

	public String getClinicalReasoning() {
		return clinicalReasoning;
	}

	public void setClinicalReasoning(String clinicalReasoning) {
		this.clinicalReasoning = clinicalReasoning;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Boolean getIsSigned() {
		return isSigned;
	}

	public void setIsSigned(Boolean isSigned) {
		this.isSigned = isSigned;
	}

}
