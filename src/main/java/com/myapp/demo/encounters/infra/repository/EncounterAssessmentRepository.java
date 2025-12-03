package com.myapp.demo.encounters.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.demo.encounters.domain.EncounterAssessment;

@Repository
public interface EncounterAssessmentRepository extends JpaRepository<EncounterAssessment, Long> {
	
}
