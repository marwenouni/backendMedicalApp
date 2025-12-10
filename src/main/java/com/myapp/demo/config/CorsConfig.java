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
	  cfg.setAllowedOriginPatterns(List.of(
			    "http://localhost:*",
			    "https://my-ehr-app-production.up.railway.app",
			    "https://mymedicalapp.netlify.app"
			  ));
	  cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
	  cfg.setAllowedHeaders(List.of("*"));
	  cfg.setAllowCredentials(false);
	  cfg.setMaxAge(3600L);

	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  source.registerCorsConfiguration("/**", cfg);
	  return source;
	}
}
