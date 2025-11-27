package com.myapp.demo.charts.infra.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.charts.domain.ChartChange;
import com.myapp.demo.entity.Chart;

public interface ChartChangeRepository extends JpaRepository<ChartChange, Long> {
  Page<ChartChange> findByChangeIdGreaterThanOrderByChangeIdAsc(long changeId, Pageable pageable);
  
}
