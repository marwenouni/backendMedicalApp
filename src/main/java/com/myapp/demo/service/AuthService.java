package com.myapp.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.myapp.demo.dto.AuthResponseDto;
import com.myapp.demo.dto.LoginDto;
import com.myapp.demo.dto.RegisterDto;
import com.myapp.demo.entity.User;

@Service
public class AuthService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
public AuthResponseDto signIn(LoginDto dto) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						dto.getEmail(), 
						dto.getPassword()));
		final User user = userService.findByEmail(dto.getEmail());
		return new AuthResponseDto(jwtService.GenerateToken(user.getEmail()));
	}

public AuthResponseDto register(RegisterDto dto) {
	
	User user = new User();
	user.setRole("ADMIN");
	user.setEmail(dto.getEmail());
	user.setPassword(passwordEncoder.encode(dto.getPassword()));
	user.setFirstName(dto.getFirstName());
	user.setLastName(dto.getLastName());
	user.setCompany(dto.getCompany());
	user.setId(null);
	
	user = userService.create(user);
	
	return new AuthResponseDto(jwtService.GenerateToken(user.getEmail()));
}
	
	
}
