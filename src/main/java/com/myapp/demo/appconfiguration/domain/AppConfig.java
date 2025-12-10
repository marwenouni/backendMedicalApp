package com.myapp.demo.appconfiguration.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_config", indexes = {
	@Index(name = "idx_config_id", columnList = "config_id", unique = true),
	@Index(name = "idx_version", columnList = "version")
})
@Getter
@Setter
public class AppConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_cabinet", nullable = false)
	private Long cabinetId;

	@Column(name = "id_provider", nullable = false)
	private Long providerId;

	@Column(name = "config_id", unique = true, nullable = false, length = 100)
	private String configId;

	@Column(name = "version", nullable = false)
	private Integer version;

	@Column(name = "timestamp", nullable = false)
	private Instant timestamp;

	@Column(name = "last_updated", nullable = false)
	private Instant lastUpdated;

	@Column(name = "updated_by", length = 255)
	private String updatedBy;

	@Column(name = "checksum", length = 64)
	private String checksum;

	@Column(name = "config_data", nullable = false, columnDefinition = "LONGTEXT")
	private String configData;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	public AppConfig() {
		this.createdAt = Instant.now();
		this.lastUpdated = Instant.now();
		this.timestamp = Instant.now();
	}

	public AppConfig(Long cabinetId, Long providerId, String configId, Integer version, String configData, String updatedBy) {
		this();
		this.cabinetId = cabinetId;
		this.providerId = providerId;
		this.configId = configId;
		this.version = version;
		this.configData = configData;
		this.updatedBy = updatedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(Long cabinetId) {
		this.cabinetId = cabinetId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Instant getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Instant lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getConfigData() {
		return configData;
	}

	public void setConfigData(String configData) {
		this.configData = configData;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
