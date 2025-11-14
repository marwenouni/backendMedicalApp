package com.myapp.demo.dto;


import lombok.Data;

@Data
public class BookDTO {
  private Long cabinetId;
  private Long patientId;
  private String startAt;   // ISO instant
  private String endAt;     // ISO instant
  private String motif;
  private String clientUuid;
public Long getCabinetId() {
	return cabinetId;
}
public void setCabinetId(Long cabinetId) {
	this.cabinetId = cabinetId;
}
public Long getPatientId() {
	return patientId;
}
public void setPatientId(Long patientId) {
	this.patientId = patientId;
}
public String getStartAt() {
	return startAt;
}
public void setStartAt(String startAt) {
	this.startAt = startAt;
}
public String getEndAt() {
	return endAt;
}
public void setEndAt(String endAt) {
	this.endAt = endAt;
}
public String getMotif() {
	return motif;
}
public void setMotif(String motif) {
	this.motif = motif;
}
public String getClientUuid() {
	return clientUuid;
}
public void setClientUuid(String clientUuid) {
	this.clientUuid = clientUuid;
}
  
}

