package com.myapp.demo.patients.infra.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.patients.domain.PatientChange;
import com.myapp.demo.entity.Patient;

public interface PatientChangeRepository extends JpaRepository<PatientChange, Long> {
  Page<PatientChange> findByChangeIdGreaterThanOrderByChangeIdAsc(long changeId, Pageable pageable);
  
}
