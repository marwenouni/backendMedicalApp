package com.myapp.demo.patients.infra.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.patients.domain.PatientClinical;

public interface PatientClinicalRepository extends JpaRepository<PatientClinical, Long> {}

