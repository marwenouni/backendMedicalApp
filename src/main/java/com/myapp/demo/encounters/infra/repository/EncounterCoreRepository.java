package com.myapp.demo.encounters.infra.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myapp.demo.encounters.domain.EncounterCore;

@Repository
public interface EncounterCoreRepository extends JpaRepository<EncounterCore, Long> {
	
	Optional<EncounterCore> findByEncounterUuid(String encounterUuid);
	
	List<EncounterCore> findByPatientId(Long patientId);
	
	List<EncounterCore> findByProviderId(Long providerId);
	
	List<EncounterCore> findByStatus(String status);
	
	@Query("SELECT e FROM EncounterCore e WHERE e.patientId = :patientId AND e.startTime >= :startDate AND e.startTime <= :endDate ORDER BY e.startTime DESC")
	List<EncounterCore> findEncountersByPatientAndDateRange(
		@Param("patientId") Long patientId,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
	
	@Query("SELECT e FROM EncounterCore e WHERE e.providerId = :providerId AND e.startTime >= :startDate AND e.startTime <= :endDate ORDER BY e.startTime DESC")
	List<EncounterCore> findEncountersByProviderAndDateRange(
		@Param("providerId") Long providerId,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
	
	@Query("SELECT e FROM EncounterCore e WHERE e.patientId = :patientId ORDER BY e.startTime DESC LIMIT 1")
	Optional<EncounterCore> findLatestEncounterByPatientId(@Param("patientId") Long patientId);
	
	List<EncounterCore> findByEncounterType(String encounterType);
	
	List<EncounterCore> findByFacilityId(Long facilityId);
	
}
