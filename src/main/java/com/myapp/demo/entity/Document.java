// com.myapp.demo.entity.Document.java
package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity @Table(name = "document")
@Data
public class Document {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long idCabinet;          // optionnel pour multi-tenant
  private Long idChart;
  private Long idConsultation;

  private String type;             // PHOTO | COMPTE_RENDU | ORDONNANCE | AUTRE
  private String originalName;
  private String storedName;
  private String mimeType;
  private Long   sizeBytes;
  private String checksumSha256;
  private String storagePath;

  private String createdBy;

  @Column(columnDefinition = "datetime")
  private LocalDateTime createdAt;

  @Column(columnDefinition = "datetime")
  private LocalDateTime updatedAt;
  
  public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Long getIdCabinet() {
	return idCabinet;
}
public void setIdCabinet(Long idCabinet) {
	this.idCabinet = idCabinet;
}
public Long getIdChart() {
	return idChart;
}
public void setIdChart(Long idChart) {
	this.idChart = idChart;
}
public Long getIdConsultation() {
	return idConsultation;
}
public void setIdConsultation(Long idConsultation) {
	this.idConsultation = idConsultation;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getOriginalName() {
	return originalName;
}
public void setOriginalName(String originalName) {
	this.originalName = originalName;
}
public String getStoredName() {
	return storedName;
}
public void setStoredName(String storedName) {
	this.storedName = storedName;
}
public String getMimeType() {
	return mimeType;
}
public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
}
public Long getSizeBytes() {
	return sizeBytes;
}
public void setSizeBytes(Long sizeBytes) {
	this.sizeBytes = sizeBytes;
}
public String getChecksumSha256() {
	return checksumSha256;
}
public void setChecksumSha256(String checksumSha256) {
	this.checksumSha256 = checksumSha256;
}
public String getStoragePath() {
	return storagePath;
}
public void setStoragePath(String storagePath) {
	this.storagePath = storagePath;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public LocalDateTime getCreatedAt() {
	return createdAt;
}
public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}
public LocalDateTime getUpdatedAt() {
	return updatedAt;
}
public void setUpdatedAt(LocalDateTime updatedAt) {
	this.updatedAt = updatedAt;
}


  @PrePersist
  public void prePersist() {
    var now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }
  @PreUpdate
  public void preUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
