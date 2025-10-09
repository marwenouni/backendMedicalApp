package com.myapp.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.myapp.demo.dto.AuthResponseDto;
import com.myapp.demo.dto.LoginDto;
import com.myapp.demo.dto.RegisterDto;
import com.myapp.demo.service.AuthService;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class JwtAuthenticationController {


	@Autowired
	private AuthService authService; 
	

//	public JwtAuthenticationController(AuthenticationManager authenticationManager,
//			JwtUserDetailsService jwtInMemoryUserDetailsService, JwtTokenUtil jwtAuthUtil) {
//		super();
//		this.authenticationManager = authenticationManager;
//		this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
//		this.jwtAuthUtil = jwtAuthUtil;
//	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterDto dto) {
		return ResponseEntity.ok(authService.register(dto));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> CreateAuthToken(@RequestBody LoginDto loginDto) throws Exception {
		
		return ResponseEntity.ok(authService.signIn(loginDto));
		
		//		String token;
//		Authentication authentication;
//		try {
//			 authentication = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
//			//SecurityContextHolder.getContext().setAuthentication(authentication);
//			System.out.println(authentication.isAuthenticated());
//
//		} catch (DisabledException e) {
//			System.out.println("USER_DISABLED "+e);
//			throw new Exception("USER_DISABLED", e);
//		} catch (BadCredentialsException e) {
//			System.out.println("BadCredentialsException "+e);
//			throw new Exception("INVALID_CREDENTIALS", e);
//		}
//		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(loginDto.getUserName());
//		token = jwtAuthUtil.GenerateToken(userDetails);
//		return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

	}
}
