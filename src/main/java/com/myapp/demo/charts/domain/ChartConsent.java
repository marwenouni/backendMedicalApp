package com.myapp.demo.charts.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="chart_consent")
@Getter @Setter
public class ChartConsent {
@Id private Long id;
@MapsId @OneToOne @JoinColumn(name="core_id") private ChartCore core;

@Column(name="hipaa_consent_signed")      private Boolean hipaaConsentSigned = Boolean.FALSE;
@Column(name="hipaa_consent_at")          private Instant hipaaConsentAt;

@Column(name="telehealth_consent_signed") private Boolean telehealthConsentSigned = Boolean.FALSE;
@Column(name="telehealth_consent_at")     private Instant telehealthConsentAt;

@Column(name="data_sharing_consent")      private Boolean dataSharingConsent = Boolean.FALSE;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public ChartCore getCore() {
	return core;
}

public void setCore(ChartCore core) {
	this.core = core;
}

public Boolean getHipaaConsentSigned() {
	return hipaaConsentSigned;
}

public void setHipaaConsentSigned(Boolean hipaaConsentSigned) {
	this.hipaaConsentSigned = hipaaConsentSigned;
}

public Instant getHipaaConsentAt() {
	return hipaaConsentAt;
}

public void setHipaaConsentAt(Instant hipaaConsentAt) {
	this.hipaaConsentAt = hipaaConsentAt;
}

public Boolean getTelehealthConsentSigned() {
	return telehealthConsentSigned;
}

public void setTelehealthConsentSigned(Boolean telehealthConsentSigned) {
	this.telehealthConsentSigned = telehealthConsentSigned;
}

public Instant getTelehealthConsentAt() {
	return telehealthConsentAt;
}

public void setTelehealthConsentAt(Instant telehealthConsentAt) {
	this.telehealthConsentAt = telehealthConsentAt;
}

public Boolean getDataSharingConsent() {
	return dataSharingConsent;
}

public void setDataSharingConsent(Boolean dataSharingConsent) {
	this.dataSharingConsent = dataSharingConsent;
}


}

