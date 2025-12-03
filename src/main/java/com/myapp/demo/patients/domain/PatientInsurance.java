package com.myapp.demo.patients.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity @Table(name="patient_insurance")
@Getter @Setter
public class PatientInsurance {
@Id private Long id;
@MapsId @OneToOne @JoinColumn(name="core_id") private PatientCore core;

@Column(name="provider", length=120)  private String provider;
@Column(name="member_id", length=80)  private String memberId;
@Column(name="plan_name", length=120) private String planName;
@Column(name="expiration_date")       private LocalDate expirationDate;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public PatientCore getCore() {
	return core;
}
public void setCore(PatientCore core) {
	this.core = core;
}
public String getProvider() {
	return provider;
}
public void setProvider(String provider) {
	this.provider = provider;
}
public String getMemberId() {
	return memberId;
}
public void setMemberId(String memberId) {
	this.memberId = memberId;
}
public String getPlanName() {
	return planName;
}
public void setPlanName(String planName) {
	this.planName = planName;
}
public LocalDate getExpirationDate() {
	return expirationDate;
}
public void setExpirationDate(LocalDate expirationDate) {
	this.expirationDate = expirationDate;
}


}
