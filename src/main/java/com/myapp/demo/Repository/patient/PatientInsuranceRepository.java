package com.myapp.demo.Repository.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.patient.PatientInsurance;

public interface PatientInsuranceRepository extends JpaRepository<PatientInsurance, Long> {}

