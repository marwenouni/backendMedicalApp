package com.myapp.demo.encounters.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EncounterCreateUpdateDto(
	String encouterUuid,
	Long patientId,
	Long providerId,
	Long idCabinet,
	Long facilityId,
	String encounterType,
	String status,
	LocalDateTime startTime,
	LocalDateTime endTime,
	Integer durationMinutes,
	String chiefComplaint,
	
	// Clinical
	String historyOfPresentIllness,
	String reviewOfSystems,
	String relevantPMH,
	List<String> medicationsReviewed,
	String physicalExamination,
	List<String> diagnosisIcd10Codes,
	List<String> diagnosisDescriptions,
	List<String> procedureCptCodes,
	List<String> procedureDescriptions,
	List<String> snomedCtCodes,
	List<String> prescribedMedications,
	List<String> labOrderCodes,
	List<String> imagingOrderCodes,
	List<String> referralSpecialties,
	String patientEducation,
	
	// Assessment
	String assessment,
	String plan,
	String followupPlan,
	String followupTimeframe,
	String prognosis,
	String goalsOfCare,
	String clinicalReasoning,
	String disposition,
	String providerName,
	Boolean isSigned,
	
	// Vitals
	LocalDateTime vitalsTime,
	Integer bpSystolic,
	Integer bpDiastolic,
	Integer heartRate,
	Integer respiratoryRate,
	Double temperatureF,
	Integer oxygenSaturation,
	String o2Method,
	Double weight,
	String weightUnit,
	Double height,
	String heightUnit,
	Double bmi,
	Integer painScore,
	Integer glucoseReading,
	String glucoseUnit,
	String vitalsNotes
) {
}
