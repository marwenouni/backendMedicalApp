package com.myapp.demo.Repository.patient;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.patient.PatientClinical;

public interface PatientClinicalRepository extends JpaRepository<PatientClinical, Long> {}

