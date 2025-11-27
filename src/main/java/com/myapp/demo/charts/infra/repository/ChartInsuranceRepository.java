package com.myapp.demo.charts.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.charts.domain.ChartInsurance;

public interface ChartInsuranceRepository extends JpaRepository<ChartInsurance, Long> {}

