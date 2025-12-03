package com.myapp.demo.encounters.app.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myapp.demo.encounters.api.dto.EncounterCreateUpdateDto;
import com.myapp.demo.encounters.api.dto.EncounterDto;

public interface IEncounterService {

	List<EncounterDto> getAllEncounters();

	EncounterDto getEncounterById(Long id);

	EncounterDto add(EncounterCreateUpdateDto dto);

	EncounterDto update(Long id, EncounterCreateUpdateDto dto);

	void delete(Long id);

	List<EncounterDto> findByPatient(Long patientId);

	Page<EncounterDto> findByPatientPaged(Long patientId, Pageable pageable);

	EncounterDto createIdempotent(EncounterCreateUpdateDto dto, String encounterUuid);

	Optional<EncounterDto> findByEncounterUuid(String encounterUuid);

	Page<EncounterDto> findByUpdatedAtAfter(Instant since, Pageable paging);

	Page<EncounterDto> findAll(Pageable paging);

	List<EncounterDto> findByIdCabinet(Long idCabinet);

	Page<EncounterDto> findByIdCabinetPaged(Long idCabinet, Pageable pageable);

	List<EncounterDto> findUpdatedSince(Instant sinceInstant);

	// Updated since queries with filters
	List<EncounterDto> findUpdatedSinceByIdCabinet(Instant since, Long idCabinet);

	Page<EncounterDto> findUpdatedSinceByIdCabinetPaged(Instant since, Long idCabinet, Pageable pageable);

	List<EncounterDto> findUpdatedSinceByProviderId(Instant since, Long providerId);

	Page<EncounterDto> findUpdatedSinceByProviderIdPaged(Instant since, Long providerId, Pageable pageable);

	List<EncounterDto> findUpdatedSinceByIdCabinetAndProviderId(Instant since, Long idCabinet, Long providerId);

	Page<EncounterDto> findUpdatedSinceByIdCabinetAndProviderIdPaged(Instant since, Long idCabinet, Long providerId, Pageable pageable);

}
