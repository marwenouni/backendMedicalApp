package com.myapp.demo.appconfiguration.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.demo.appconfiguration.domain.AppConfig;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
	Optional<AppConfig> findByConfigId(String configId);

	Optional<AppConfig> findFirstByOrderByVersionDesc();

	Optional<AppConfig> findByCabinetIdAndProviderIdOrderByVersionDesc(Long cabinetId, Long providerId);
}
