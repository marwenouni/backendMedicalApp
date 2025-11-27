package com.myapp.demo.charts.infra.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.charts.domain.ChartClinical;

public interface ChartClinicalRepository extends JpaRepository<ChartClinical, Long> {}

