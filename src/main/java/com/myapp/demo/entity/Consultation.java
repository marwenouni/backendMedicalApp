package com.myapp.demo.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Component
@Data
@Entity
@Table(name = "consultation")
public class Consultation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer idCabinet;
	private Integer idPatient;
	private String observation;
	private String traitement;
	private String examen;
	private String date;
	
	public Consultation(){
		
	}
	public Consultation(Integer idCabinet, Integer idPatient, String observation, String traitement, String examen,
			String date) {
		super();
		this.idCabinet = idCabinet;
		this.idPatient = idPatient;
		this.observation = observation;
		this.traitement = traitement;
		this.examen = examen;
		this.date = date;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdCabinet() {
		return idCabinet;
	}
	public void setIdCabinet(Integer idCabinet) {
		this.idCabinet = idCabinet;
	}
	public Integer getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(Integer idPatient) {
		this.idPatient = idPatient;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public String getTraitement() {
		return traitement;
	}
	public void setTraitement(String traitement) {
		this.traitement = traitement;
	}
	public String getExamen() {
		return examen;
	}
	public void setExamen(String examen) {
		this.examen = examen;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	

}
