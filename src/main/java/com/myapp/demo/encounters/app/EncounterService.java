package com.myapp.demo.encounters.app;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.demo.encounters.api.dto.EncounterCreateUpdateDto;
import com.myapp.demo.encounters.api.dto.EncounterDto;
import com.myapp.demo.encounters.domain.EncounterAssessment;
import com.myapp.demo.encounters.domain.EncounterCore;
import com.myapp.demo.encounters.domain.EncounterClinical;
import com.myapp.demo.encounters.domain.EncounterVitals;
import com.myapp.demo.encounters.infra.repository.EncounterAssessmentRepository;
import com.myapp.demo.encounters.infra.repository.EncounterClinicalRepository;
import com.myapp.demo.encounters.infra.repository.EncounterCoreRepository;
import com.myapp.demo.encounters.infra.repository.EncounterVitalsRepository;
import com.myapp.demo.patients.api.PatientEventsController;
import com.myapp.demo.patients.infra.repository.PatientClinicalRepository;
import com.myapp.demo.patients.infra.repository.PatientConsentRepository;
import com.myapp.demo.patients.infra.repository.PatientCoreRepository;
import com.myapp.demo.patients.infra.repository.PatientIdentityRepository;
import com.myapp.demo.patients.infra.repository.PatientInsuranceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EncounterService {

	PatientEventsController sse;
	private final EncounterCoreRepository coreRepository;
	private final EncounterClinicalRepository clinicalRepository;
	private final EncounterAssessmentRepository assessmentRepository;
	private final EncounterVitalsRepository vitalsRepository;
	
	@Autowired
	public EncounterService(EncounterCoreRepository coreRepository, EncounterClinicalRepository clinicalRepository,
			EncounterAssessmentRepository assessmentRepository, EncounterVitalsRepository vitalsRepository,
			PatientEventsController sse) {
		this.coreRepository = coreRepository;
		this.clinicalRepository = clinicalRepository;
		this.assessmentRepository = assessmentRepository;
		this.vitalsRepository = vitalsRepository;
		this.sse = sse;
	}

	public EncounterDto createEncounter(EncounterCreateUpdateDto dto) {
		// Create core
		EncounterCore core = new EncounterCore();
		core.setEncounterUuid(UUID.randomUUID().toString());
		core.setPatientId(dto.patientId());
		core.setProviderId(dto.providerId());
		core.setFacilityId(dto.facilityId());
		core.setEncounterType(dto.encounterType());
		core.setStatus(dto.status());
		core.setStartTime(dto.startTime());
		core.setEndTime(dto.endTime());
		core.setDurationMinutes(dto.durationMinutes());
		core.setChiefComplaint(dto.chiefComplaint());

		// Create clinical
		EncounterClinical clinical = new EncounterClinical();
		clinical.setCore(core);
		clinical.setHistoryOfPresentIllness(dto.historyOfPresentIllness());
		clinical.setReviewOfSystems(dto.reviewOfSystems());
		clinical.setRelevantPMH(dto.relevantPMH());
		clinical.setMedicationsReviewed(dto.medicationsReviewed());
		clinical.setPhysicalExamination(dto.physicalExamination());
		clinical.setDiagnosisIcd10Codes(dto.diagnosisIcd10Codes());
		clinical.setDiagnosisDescriptions(dto.diagnosisDescriptions());
		clinical.setProcedureCptCodes(dto.procedureCptCodes());
		clinical.setProcedureDescriptions(dto.procedureDescriptions());
		clinical.setSnomedCtCodes(dto.snomedCtCodes());
		clinical.setPrescribedMedications(dto.prescribedMedications());
		clinical.setLabOrderCodes(dto.labOrderCodes());
		clinical.setImagingOrderCodes(dto.imagingOrderCodes());
		clinical.setReferralSpecialties(dto.referralSpecialties());
		clinical.setPatientEducation(dto.patientEducation());

		// Create assessment
		EncounterAssessment assessment = new EncounterAssessment();
		assessment.setCore(core);
		assessment.setAssessment(dto.assessment());
		assessment.setPlan(dto.plan());
		assessment.setFollowupPlan(dto.followupPlan());
		assessment.setFollowupTimeframe(dto.followupTimeframe());
		assessment.setPrognosis(dto.prognosis());
		assessment.setGoalsOfCare(dto.goalsOfCare());
		assessment.setClinicalReasoning(dto.clinicalReasoning());
		assessment.setDisposition(dto.disposition());
		assessment.setProviderName(dto.providerName());
		assessment.setIsSigned(dto.isSigned() != null ? dto.isSigned() : false);

		// Create vitals
		EncounterVitals vitals = new EncounterVitals();
		vitals.setCore(core);
		vitals.setVitalsTime(dto.vitalsTime());
		vitals.setBpSystolic(dto.bpSystolic());
		vitals.setBpDiastolic(dto.bpDiastolic());
		vitals.setHeartRate(dto.heartRate());
		vitals.setRespiratoryRate(dto.respiratoryRate());
		vitals.setTemperatureF(dto.temperatureF());
		vitals.setOxygenSaturation(dto.oxygenSaturation());
		vitals.setO2Method(dto.o2Method());
		vitals.setWeight(dto.weight());
		vitals.setWeightUnit(dto.weightUnit());
		vitals.setHeight(dto.height());
		vitals.setHeightUnit(dto.heightUnit());
		vitals.setBmi(dto.bmi());
		vitals.setPainScore(dto.painScore());
		vitals.setGlucoseReading(dto.glucoseReading());
		vitals.setGlucoseUnit(dto.glucoseUnit());
		vitals.setVitalsNotes(dto.vitalsNotes());

		core.setClinical(clinical);
		core.setAssessment(assessment);
		core.setVitals(vitals);

		EncounterCore saved = coreRepository.save(core);
		return toDto(saved);
	}

	public EncounterDto getEncounterById(Long id) {
		return coreRepository.findById(id)
			.map(this::toDto)
			.orElse(null);
	}

	public EncounterDto getEncounterByUuid(String uuid) {
		return coreRepository.findByEncounterUuid(uuid)
			.map(this::toDto)
			.orElse(null);
	}

	public void deleteEncounter(Long id) {
		coreRepository.deleteById(id);
	}

	private EncounterDto toDto(EncounterCore core) {
		if (core == null) {
			return null;
		}

		EncounterClinical clinical = core.getClinical();
		EncounterAssessment assessment = core.getAssessment();
		EncounterVitals vitals = core.getVitals();

		return new EncounterDto(
			core.getId(),
			core.getIdCabinet(),
			core.getEncounterUuid(),
			core.getPatientId(),
			core.getProviderId(),
			core.getFacilityId(),
			core.getEncounterType(),
			core.getStatus(),
			core.getStartTime(),
			core.getEndTime(),
			core.getDurationMinutes(),
			core.getChiefComplaint(),
			
			clinical != null ? clinical.getHistoryOfPresentIllness() : null,
			clinical != null ? clinical.getReviewOfSystems() : null,
			clinical != null ? clinical.getRelevantPMH() : null,
			clinical != null ? clinical.getMedicationsReviewed() : null,
			clinical != null ? clinical.getPhysicalExamination() : null,
			clinical != null ? clinical.getDiagnosisIcd10Codes() : null,
			clinical != null ? clinical.getDiagnosisDescriptions() : null,
			clinical != null ? clinical.getProcedureCptCodes() : null,
			clinical != null ? clinical.getProcedureDescriptions() : null,
			clinical != null ? clinical.getSnomedCtCodes() : null,
			clinical != null ? clinical.getPrescribedMedications() : null,
			clinical != null ? clinical.getLabOrderCodes() : null,
			clinical != null ? clinical.getImagingOrderCodes() : null,
			clinical != null ? clinical.getReferralSpecialties() : null,
			clinical != null ? clinical.getPatientEducation() : null,
			
			assessment != null ? assessment.getAssessment() : null,
			assessment != null ? assessment.getPlan() : null,
			assessment != null ? assessment.getFollowupPlan() : null,
			assessment != null ? assessment.getFollowupTimeframe() : null,
			assessment != null ? assessment.getPrognosis() : null,
			assessment != null ? assessment.getGoalsOfCare() : null,
			assessment != null ? assessment.getClinicalReasoning() : null,
			assessment != null ? assessment.getDisposition() : null,
			assessment != null ? assessment.getProviderName() : null,
			assessment != null ? assessment.getIsSigned() : null,
			
			vitals != null ? vitals.getVitalsTime() : null,
			vitals != null ? vitals.getBpSystolic() : null,
			vitals != null ? vitals.getBpDiastolic() : null,
			vitals != null ? vitals.getHeartRate() : null,
			vitals != null ? vitals.getRespiratoryRate() : null,
			vitals != null ? vitals.getTemperatureF() : null,
			vitals != null ? vitals.getOxygenSaturation() : null,
			vitals != null ? vitals.getO2Method() : null,
			vitals != null ? vitals.getWeight() : null,
			vitals != null ? vitals.getWeightUnit() : null,
			vitals != null ? vitals.getHeight() : null,
			vitals != null ? vitals.getHeightUnit() : null,
			vitals != null ? vitals.getBmi() : null,
			vitals != null ? vitals.getPainScore() : null,
			vitals != null ? vitals.getGlucoseReading() : null,
			vitals != null ? vitals.getGlucoseUnit() : null,
			vitals != null ? vitals.getVitalsNotes() : null,
			
			core.getCreatedAt(),
			core.getUpdatedAt(),
			core.getVersion()
		);
	}

}
