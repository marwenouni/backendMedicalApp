package com.myapp.demo.patients.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "patient_changes")
public class PatientChange {

  @Id
  @Column(name = "change_id")
  private Long changeId;

  @Column(name = "row_id", nullable = false)
  private Long rowId;

  // Colonne ENUM('I','U','D') en base → côté Java on la manipule comme String (1 char)
  @Column(name = "op", nullable = false, length = 1)
  private String op;

  @Column(name = "changed_at", nullable = false)
  private Instant changedAt;

  public PatientChange() {}

  // --- getters/setters requis par ton code ---
  public Long getChangeId() { return changeId; }
  public void setChangeId(Long changeId) { this.changeId = changeId; }

  public Long getRowId() { return rowId; }
  public void setRowId(Long rowId) { this.rowId = rowId; }

  public String getOp() { return op; }
  public void setOp(String op) { this.op = op; }

  public Instant getChangedAt() { return changedAt; }
  public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
}
