package com.myapp.demo.entity;


import java.sql.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.*;
import lombok.Data;

@Component
@Data
@Entity
@Table(name = "patient")
public class Patient {
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer idCabinet;
	private String firstName;
	private String lastName;
	private String birthday;
	private String numberPhone;
	private String email;
	private String city;
	@Column(name = "client_uuid", nullable = false, unique = true)
	private String clientUuid;

	public Patient(){}
	public Patient(Integer id2,Integer idCabinet2,String firstname2, String lastname2, String birthday2, String numberPhone2,
			String email2, String city2) {
		this.setId(id2);
		this.setIdCabinet(idCabinet2);
		this.setFirstName(firstname2);
		this.setLastName(lastname2);
		this.setBirthday(birthday2);
		this.setNumberPhone(numberPhone2);
		this.setEmail(email2);
		this.setCity(city2);
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

	public void setIdCabinet(Integer id_cabinet) {
		this.idCabinet = id_cabinet;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNumberPhone() {
		return numberPhone;
	}

	public void setNumberPhone(String numberPhone) {
		this.numberPhone = numberPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getClientUuid() { return clientUuid; }
	
	public void setClientUuid(String clientUuid) { this.clientUuid = clientUuid; }
}
