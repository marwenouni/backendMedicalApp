package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProviderLicense {

    /** État (code 2 lettres) */
    @Column(name = "lic_state", length = 2)
    private String state;              // ex: CA, NY, TX

    /** Numéro de licence d’exercice */
    @Column(name = "lic_number", length = 64)
    private String licenseNumber;

    /** Date d’expiration */
    @Column(name = "lic_expires_on")
    private LocalDate expiresOn;

    /** Compact (Interstate Medical Licensure Compact) */
    @Column(name = "lic_imlc_member")
    private Boolean imlcMember;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public LocalDate getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(LocalDate expiresOn) {
		this.expiresOn = expiresOn;
	}

	public Boolean getImlcMember() {
		return imlcMember;
	}

	public void setImlcMember(Boolean imlcMember) {
		this.imlcMember = imlcMember;
	}
    
    
}
