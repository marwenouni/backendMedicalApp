package com.myapp.demo.service;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.myapp.demo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	
	@Value("${jwt.secret.key}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;

	private static final long serialVersionUID = -2550185165626007488L;
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).before(new Date());
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

	}

	
	public String GenerateToken(String userName)
	{
		return  Jwts.builder()
				.setSubject(userName)
				.setIssuedAt( new Date())
				.setExpiration(new Date(System.currentTimeMillis()+expiration*1000))
				.signWith(SignatureAlgorithm.HS512,secret)
				.compact();
		
	}
}
