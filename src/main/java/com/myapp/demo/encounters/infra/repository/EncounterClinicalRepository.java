package com.myapp.demo.encounters.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.demo.encounters.domain.EncounterClinical;

@Repository
public interface EncounterClinicalRepository extends JpaRepository<EncounterClinical, Long> {
	
}
