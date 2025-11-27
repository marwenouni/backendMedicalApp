package com.myapp.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rendez_vous",
       indexes = {
         @Index(columnList = "cabinet_id,start_at"),
         @Index(columnList = "chart_id"),
         @Index(columnList = "client_uuid", unique = true)
       })
public class RendezVous {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "cabinet_id", nullable = false)
  private Long cabinetId;

  @Column(name = "chart_id", nullable = false)
  private Long chartId;

  @Column(name = "start_at", nullable = false)
  private Instant startAt;

  @Column(name = "end_at", nullable = false)
  private Instant endAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private Status status = Status.BOOKED;

  @Column(columnDefinition = "varchar(512)")
  private String motif;

  @Column(name = "client_uuid", length = 64, unique = true)
  private String clientUuid;

  @Column(name = "moved_from_id")
  private Long movedFromId;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  public enum Status { BOOKED, CANCELLED, NOSHOW }

  @PreUpdate
  public void onUpdate() { this.updatedAt = Instant.now(); }

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

public Long getChartId() {
	return chartId;
}

public void setChartId(Long chartId) {
	this.chartId = chartId;
}

public Instant getStartAt() {
	return startAt;
}

public void setStartAt(Instant startAt) {
	this.startAt = startAt;
}

public Instant getEndAt() {
	return endAt;
}

public void setEndAt(Instant endAt) {
	this.endAt = endAt;
}

public Status getStatus() {
	return status;
}

public void setStatus(Status status) {
	this.status = status;
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

public Long getMovedFromId() {
	return movedFromId;
}

public void setMovedFromId(Long movedFromId) {
	this.movedFromId = movedFromId;
}

public Instant getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
}

public Instant getUpdatedAt() {
	return updatedAt;
}

public void setUpdatedAt(Instant updatedAt) {
	this.updatedAt = updatedAt;
}
  
  

  
}
