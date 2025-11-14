package com.myapp.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "unavailability", indexes = @Index(columnList = "cabinet_id,start_at"))
public class Unavailability {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "cabinet_id", nullable = false)
  private Long cabinetId;

  @Column(name = "start_at", nullable = false)
  private Instant startAt;

  @Column(name = "end_at", nullable = false)
  private Instant endAt;

  @Column(columnDefinition = "varchar(256)")
  private String reason;

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

public String getReason() {
	return reason;
}

public void setReason(String reason) {
	this.reason = reason;
}

  
}

