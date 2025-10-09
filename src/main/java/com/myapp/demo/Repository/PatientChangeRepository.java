package com.myapp.demo.Repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.Patient;
import com.myapp.demo.entity.PatientChange;

public interface PatientChangeRepository extends JpaRepository<PatientChange, Long> {
  Page<PatientChange> findByChangeIdGreaterThanOrderByChangeIdAsc(long changeId, Pageable pageable);
  
}
