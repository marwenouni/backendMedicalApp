package com.myapp.demo.Repository.patient;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.patient.PatientConsent;

public interface PatientConsentRepository extends JpaRepository<PatientConsent, Long> {}

