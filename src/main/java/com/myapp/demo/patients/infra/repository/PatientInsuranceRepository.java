package com.myapp.demo.patients.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.patients.domain.PatientInsurance;

public interface PatientInsuranceRepository extends JpaRepository<PatientInsurance, Long> {}

