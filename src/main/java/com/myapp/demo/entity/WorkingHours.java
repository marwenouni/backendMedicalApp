package com.myapp.demo.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "working_hours", indexes = @Index(columnList = "cabinet_id,weekday"))
public class WorkingHours {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "cabinet_id", nullable = false)
  private Long cabinetId;

  /** 1..7 (Mon..Sun) */
  @Column(nullable = false)
  private int weekday;

  @Column(nullable = false)
  private LocalTime start;

  @Column(nullable = false)
  private LocalTime end;

  @Column(name = "slot_minutes", nullable = false)
  private Integer slotMinutes = 15;

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

public int getWeekday() {
	return weekday;
}

public void setWeekday(int weekday) {
	this.weekday = weekday;
}

public LocalTime getStart() {
	return start;
}

public void setStart(LocalTime start) {
	this.start = start;
}

public LocalTime getEnd() {
	return end;
}

public void setEnd(LocalTime end) {
	this.end = end;
}

public Integer getSlotMinutes() {
	return slotMinutes;
}

public void setSlotMinutes(Integer slotMinutes) {
	this.slotMinutes = slotMinutes;
}


}

