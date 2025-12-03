package com.myapp.demo.encounters.domain;

import java.time.LocalDateTime;

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
@Table(name = "encounter_vitals")
@Getter
@Setter
public class EncounterVitals {

	@Id
	private Long id;

	@MapsId
	@OneToOne
	@JoinColumn(name = "core_id")
	private EncounterCore core;

	@Column(name = "vitals_time")
	private LocalDateTime vitalsTime;

	// Blood Pressure (systolic/diastolic in mmHg)
	@Column(name = "bp_systolic")
	private Integer bpSystolic;

	@Column(name = "bp_diastolic")
	private Integer bpDiastolic;

	// Heart Rate (bpm)
	@Column(name = "heart_rate")
	private Integer heartRate;

	// Respiratory Rate (breaths per minute)
	@Column(name = "respiratory_rate")
	private Integer respiratoryRate;

	// Temperature (Fahrenheit)
	@Column(name = "temperature_f")
	private Double temperatureF;

	// Oxygen Saturation (percent)
	@Column(name = "oxygen_saturation")
	private Integer oxygenSaturation;

	// Oxygen saturation method (e.g., "Room Air", "On 2L O2")
	@Column(name = "o2_method", length = 100)
	private String o2Method;

	// Weight (lbs or kg)
	@Column(name = "weight")
	private Double weight;

	@Column(name = "weight_unit", length = 10)
	private String weightUnit;

	// Height (inches or cm)
	@Column(name = "height")
	private Double height;

	@Column(name = "height_unit", length = 10)
	private String heightUnit;

	// BMI calculated
	@Column(name = "bmi")
	private Double bmi;

	// Pain score (0-10)
	@Column(name = "pain_score")
	private Integer painScore;

	// Glucose reading (if relevant)
	@Column(name = "glucose_reading")
	private Integer glucoseReading;

	@Column(name = "glucose_unit", length = 20)
	private String glucoseUnit;

	// Notes about vitals (e.g., patient anxiety)
	@Column(name = "vitals_notes", length = 256)
	private String vitalsNotes;

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

	public LocalDateTime getVitalsTime() {
		return vitalsTime;
	}

	public void setVitalsTime(LocalDateTime vitalsTime) {
		this.vitalsTime = vitalsTime;
	}

	public Integer getBpSystolic() {
		return bpSystolic;
	}

	public void setBpSystolic(Integer bpSystolic) {
		this.bpSystolic = bpSystolic;
	}

	public Integer getBpDiastolic() {
		return bpDiastolic;
	}

	public void setBpDiastolic(Integer bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}

	public Integer getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}

	public Integer getRespiratoryRate() {
		return respiratoryRate;
	}

	public void setRespiratoryRate(Integer respiratoryRate) {
		this.respiratoryRate = respiratoryRate;
	}

	public Double getTemperatureF() {
		return temperatureF;
	}

	public void setTemperatureF(Double temperatureF) {
		this.temperatureF = temperatureF;
	}

	public Integer getOxygenSaturation() {
		return oxygenSaturation;
	}

	public void setOxygenSaturation(Integer oxygenSaturation) {
		this.oxygenSaturation = oxygenSaturation;
	}

	public String getO2Method() {
		return o2Method;
	}

	public void setO2Method(String o2Method) {
		this.o2Method = o2Method;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getHeightUnit() {
		return heightUnit;
	}

	public void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}

	public Double getBmi() {
		return bmi;
	}

	public void setBmi(Double bmi) {
		this.bmi = bmi;
	}

	public Integer getPainScore() {
		return painScore;
	}

	public void setPainScore(Integer painScore) {
		this.painScore = painScore;
	}

	public Integer getGlucoseReading() {
		return glucoseReading;
	}

	public void setGlucoseReading(Integer glucoseReading) {
		this.glucoseReading = glucoseReading;
	}

	public String getGlucoseUnit() {
		return glucoseUnit;
	}

	public void setGlucoseUnit(String glucoseUnit) {
		this.glucoseUnit = glucoseUnit;
	}

	public String getVitalsNotes() {
		return vitalsNotes;
	}

	public void setVitalsNotes(String vitalsNotes) {
		this.vitalsNotes = vitalsNotes;
	}

}
