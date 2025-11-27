package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {

    @Column(name = "addr_line1", length = 160)
    private String line1;

    @Column(name = "addr_line2", length = 160)
    private String line2;

    @Column(name = "addr_city", length = 80)
    private String city;

    @Column(name = "addr_state", length = 2) // US state code
    private String state;

    @Column(name = "addr_zip", length = 16)
    private String zip;

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
    
    
}
