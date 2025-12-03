package com.myapp.demo.encounters.infra.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.demo.encounters.domain.EncounterCore;

@Repository
public interface EncounterRepository extends JpaRepository<EncounterCore, Long> {

	List<EncounterCore> findAll();

	Optional<EncounterCore> findById(Long id);

	void deleteById(Long id);

	List<EncounterCore> findByPatientId(Long patientId);

	Page<EncounterCore> findByPatientIdOrderByUpdatedAtDesc(Long patientId, Pageable pageable);

	List<EncounterCore> findByUpdatedAtAfterOrderByUpdatedAtAsc(Instant since);

	Optional<EncounterCore> findByEncounterUuid(String encounterUuid);

	Page<EncounterCore> findByUpdatedAtAfter(Instant since, Pageable paging);

	Page<EncounterCore> findAll(Pageable paging);

	List<EncounterCore> findByIdCabinet(Long idCabinet);

	Page<EncounterCore> findByIdCabinetOrderByUpdatedAtDesc(Long idCabinet, Pageable pageable);

	// Updated since queries with cabinet filter
	List<EncounterCore> findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(Instant since, Long idCabinet);

	Page<EncounterCore> findByUpdatedAtAfterAndIdCabinetOrderByUpdatedAtAsc(Instant since, Long idCabinet, Pageable pageable);

	// Updated since queries with provider filter
	List<EncounterCore> findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(Instant since, Long providerId);

	Page<EncounterCore> findByUpdatedAtAfterAndProviderIdOrderByUpdatedAtAsc(Instant since, Long providerId, Pageable pageable);

	// Updated since queries with both cabinet and provider filters
	List<EncounterCore> findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(Instant since, Long idCabinet, Long providerId);

	Page<EncounterCore> findByUpdatedAtAfterAndIdCabinetAndProviderIdOrderByUpdatedAtAsc(Instant since, Long idCabinet, Long providerId, Pageable pageable);

}
