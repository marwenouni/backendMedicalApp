package com.myapp.demo.encounters.app;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.myapp.demo.encounters.api.dto.EncounterCreateUpdateDto;
import com.myapp.demo.encounters.api.dto.EncounterDto;
import com.myapp.demo.encounters.app.interfaces.IEncounterService;
import com.myapp.demo.encounters.domain.EncounterAssessment;
import com.myapp.demo.encounters.domain.EncounterCore;
import com.myapp.demo.encounters.domain.EncounterClinical;
import com.myapp.demo.encounters.domain.EncounterVitals;
import com.myapp.demo.encounters.infra.repository.EncounterRepository;

import jakarta.transaction.Transactional;

@Service
public class EncounterServiceImpl implements IEncounterService {

	@Autowired
	private EncounterRepository encounterRepository;

	@Override
	public List<EncounterDto> getAllEncounters() {
		return encounterRepository.findAll().stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public EncounterDto getEncounterById(Long id) {
		return encounterRepository.findById(id)
			.map(this::toDto)
			.orElse(null);
	}

	@Override
	@Transactional
	public EncounterDto add(EncounterCreateUpdateDto dto) {
		EncounterCore core = createCoreFromDto(dto);
		core.setEncounterUuid(UUID.randomUUID().toString());
		EncounterCore saved = encounterRepository.save(core);
		return toDto(saved);
	}

	@Override
	@Transactional
	public EncounterDto update(Long id, EncounterCreateUpdateDto dto) {
		return encounterRepository.findById(id)
			.map(core -> {
				updateCoreFromDto(core, dto);
				EncounterCore updated = encounterRepository.save(core);
				return toDto(updated);
			})
			.orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		encounterRepository.deleteById(id);
	}

	@Override
	public List<EncounterDto> findByPatient(Long patientId) {
		return encounterRepository.findByPatientId(patientId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<EncounterDto> findByPatientPaged(Long patientId, Pageable pageable) {
		return encounterRepository.findByPatientIdOrderByUpdatedAtDesc(patientId, pageable)
			.map(this::toDto);
	}

	@Override
	public List<EncounterDto> findUpdatedSince(Instant since) {
		return encounterRepository.findByUpdatedAtAfterOrderByUpdatedAtAsc(since).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public EncounterDto createIdempotent(EncounterCreateUpdateDto dto, String encounterUuid) {
		if (encounterUuid != null && !encounterUuid.isBlank()) {
			Optional<EncounterCore> existing = encounterRepository.findByEncounterUuid(encounterUuid);
			if (existing.isPresent()) {
				return toDto(existing.get());
			}
		}
		EncounterCore core = createCoreFromDto(dto);
		core.setEncounterUuid(encounterUuid != null ? encounterUuid : UUID.randomUUID().toString());
		EncounterCore saved = encounterRepository.save(core);
		return toDto(saved);
	}

	@Override
	public Optional<EncounterDto> findByEncounterUuid(String encounterUuid) {
		return encounterRepository.findByEncounterUuid(encounterUuid)
			.map(this::toDto);
	}

	@Override
	public Page<EncounterDto> findByUpdatedAtAfter(Instant since, Pageable paging) {
		return encounterRepository.findByUpdatedAtAfter(since, paging)
			.map(this::toDto);
	}

	@Override
	public Page<EncounterDto> findAll(Pageable paging) {
		return encounterRepository.findAll(paging)
			.map(this::toDto);
	}

	@Override
	public List<EncounterDto> findByIdCabinet(Long idCabinet) {
		return encounterRepository.findByIdCabinet(idCabinet).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<EncounterDto> findByIdCabinetPaged(Long idCabinet, Pageable pageable) {
		return encounterRepository.findByIdCabinetOrderByUpdatedAtDesc(idCabinet, pageable)
			.map(this::toDto);
	}

	@Override
	public List<EncounterDto> findUpdatedSinceByIdCabinet(Instant since, Long idCabinet) {
		return encounterRepository.findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(since, idCabinet).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<EncounterDto> findUpdatedSinceByIdCabinetPaged(Instant since, Long idCabinet, Pageable pageable) {
		return encounterRepository.findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(since, idCabinet, pageable)
			.map(this::toDto);
	}

	@Override
	public List<EncounterDto> findUpdatedSinceByProviderId(Instant since, Long providerId) {
		return encounterRepository.findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(since, providerId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<EncounterDto> findUpdatedSinceByProviderIdPaged(Instant since, Long providerId, Pageable pageable) {
		return encounterRepository.findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(since, providerId, pageable)
			.map(this::toDto);
	}

	@Override
	public List<EncounterDto> findUpdatedSinceByIdCabinetAndProviderId(Instant since, Long idCabinet, Long providerId) {
		return encounterRepository.findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(since, idCabinet, providerId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<EncounterDto> findUpdatedSinceByIdCabinetAndProviderIdPaged(Instant since, Long idCabinet, Long providerId, Pageable pageable) {
		return encounterRepository.findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(since, idCabinet, providerId, pageable)
			.map(this::toDto);
	}

	private EncounterCore createCoreFromDto(EncounterCreateUpdateDto dto) {
		EncounterCore core = new EncounterCore();
		core.setEncounterUuid(dto.encouterUuid());
		core.setPatientId(dto.patientId());
		core.setProviderId(dto.providerId());
		core.setIdCabinet(dto.idCabinet());
		core.setFacilityId(dto.facilityId());
		core.setEncounterType(dto.encounterType());
		core.setStatus(dto.status());
		core.setStartTime(dto.startTime());
		core.setEndTime(dto.endTime());
		core.setDurationMinutes(dto.durationMinutes());
		core.setChiefComplaint(dto.chiefComplaint());

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

		return core;
	}

	private void updateCoreFromDto(EncounterCore core, EncounterCreateUpdateDto dto) {
		core.setEncounterUuid(dto.encouterUuid());
		core.setPatientId(dto.patientId());
		core.setProviderId(dto.providerId());
		core.setIdCabinet(dto.idCabinet());
		core.setFacilityId(dto.facilityId());
		core.setEncounterType(dto.encounterType());
		core.setStatus(dto.status());
		core.setStartTime(dto.startTime());
		core.setEndTime(dto.endTime());
		core.setDurationMinutes(dto.durationMinutes());
		core.setChiefComplaint(dto.chiefComplaint());

		EncounterClinical clinical = core.getClinical();
		if (clinical == null) {
			clinical = new EncounterClinical();
			clinical.setCore(core);
		}
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
		core.setClinical(clinical);

		EncounterAssessment assessment = core.getAssessment();
		if (assessment == null) {
			assessment = new EncounterAssessment();
			assessment.setCore(core);
		}
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
		core.setAssessment(assessment);

		EncounterVitals vitals = core.getVitals();
		if (vitals == null) {
			vitals = new EncounterVitals();
			vitals.setCore(core);
		}
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
		core.setVitals(vitals);
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
