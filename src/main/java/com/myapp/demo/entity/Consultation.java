package com.myapp.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "consultation")
public class Consultation {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "id_cabinet")
  private Integer idCabinet;

  @Column(name = "id_patient", nullable = false)
  private Integer idPatient;

  @Column(columnDefinition = "TEXT")
  private String motif;
  
  @Column(columnDefinition = "TEXT")
  private String observation;

  @Column(columnDefinition = "TEXT")
  private String traitement;

  @Column(columnDefinition = "TEXT")
  private String examen;

  // Si tu as déjà des strings pour la date en base, on garde String.
  // (Sinon, préfère LocalDate/Instant)
  @Column(name = "date")
  private String date;

  // Idempotence facultative (même principe que patient.clientUuid)
  @Column(name = "client_uuid", unique = true)
  private String clientUuid;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  void onUpdate() {
    this.updatedAt = Instant.now();
  }

  // getters/setters …

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  public Integer getIdCabinet() { return idCabinet; }
  public void setIdCabinet(Integer idCabinet) { this.idCabinet = idCabinet; }

  public Integer getIdPatient() { return idPatient; }
  public void setIdPatient(Integer idPatient) { this.idPatient = idPatient; }

  public String getMotif() {
	return motif;
}

public void setMotif(String motif) {
	this.motif = motif;
}

public String getObservation() { return observation; }
  public void setObservation(String observation) { this.observation = observation; }

  public String getTraitement() { return traitement; }
  public void setTraitement(String traitement) { this.traitement = traitement; }

  public String getExamen() { return examen; }
  public void setExamen(String examen) { this.examen = examen; }

  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }

  public String getClientUuid() { return clientUuid; }
  public void setClientUuid(String clientUuid) { this.clientUuid = clientUuid; }

  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
