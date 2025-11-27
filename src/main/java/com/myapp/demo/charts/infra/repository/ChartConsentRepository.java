package com.myapp.demo.charts.infra.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.charts.domain.ChartConsent;

public interface ChartConsentRepository extends JpaRepository<ChartConsent, Long> {}

