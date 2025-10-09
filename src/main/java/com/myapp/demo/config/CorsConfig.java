package com.myapp.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	  CorsConfiguration cfg = new CorsConfiguration();
	  cfg.setAllowedOrigins(List.of("https://backendmedicalapp-production.up.railway.app")); // ton front
	  cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
	  cfg.setAllowedHeaders(List.of("authorization","content-type"));
	  cfg.setAllowCredentials(false); // laisse à false si tu n’utilises pas de cookies
	  cfg.setMaxAge(3600L);

	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  source.registerCorsConfiguration("/**", cfg);
	  return source;
	}
}


