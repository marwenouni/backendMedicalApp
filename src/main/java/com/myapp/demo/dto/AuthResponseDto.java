package com.myapp.demo.dto;

public class AuthResponseDto {

	private String accesToken;
	
	public AuthResponseDto(String accessToken) {
        this.accesToken = accessToken;
    }
	public String getAccesToken() {
		return accesToken;
	}
}
