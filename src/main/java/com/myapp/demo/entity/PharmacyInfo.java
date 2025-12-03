package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PharmacyInfo {

    @Column(name = "pharmacy_name", length = 160)
    private String name;

    @Column(name = "pharmacy_phone", length = 32)
    private String phone;

    @Column(name = "pharmacy_fax", length = 32)
    private String fax;

    @Column(name = "pharmacy_address", length = 200)
    private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
    
    
}
