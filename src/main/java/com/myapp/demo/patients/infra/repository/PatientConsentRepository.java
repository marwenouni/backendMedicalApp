package com.myapp.demo.patients.infra.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.patients.domain.PatientConsent;

public interface PatientConsentRepository extends JpaRepository<PatientConsent, Long> {}

