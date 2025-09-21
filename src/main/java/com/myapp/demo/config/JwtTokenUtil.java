//package com.myapp.demo.config;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.function.Function;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@Component
//public class JwtTokenUtil implements Serializable {
//
//	//@Value("${jwt.secret}")
//	private String secret="javainuse";
//
//	private static final long serialVersionUID = -2550185165626007488L;
//	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
//
//	public String getUsernameFromToken(String token) {
//		return getClaimFromToken(token, Claims::getSubject);
//	}
//
//	public Date getIssuedAtDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getIssuedAt);
//	}
//
//	public Date getExpirationDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getExpiration);
//	}
//
//	public boolean isTokenExpired(String token) {
//		return getExpirationDateFromToken(token).before(new Date());
//	}
//
//	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//		final Claims claims = getAllClaimsFromToken(token);
//		return claimsResolver.apply(claims);
//	}
//
//	public Claims getAllClaimsFromToken(String token) {
//		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//
//	}
//	
//
//	
////	public String doGenerateToken(String object)
////	{
////		return  Jwts.builder()
////				.setSubject(object)
////				.setIssuedAt( new Date())
////				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
////				.signWith(SignatureAlgorithm.HS512,secret)
////				.compact();
////		
////	}
//	
//	public String GenerateToken(UserDetails user)
//	{
//		return  Jwts.builder()
//				.setSubject(user.getUsername())
//				.setIssuedAt( new Date())
//				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
//				.signWith(SignatureAlgorithm.HS512,secret)
//				.compact();
//		
//	}
//}
