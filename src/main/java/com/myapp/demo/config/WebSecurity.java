package com.myapp.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {

  private final UserDetailsService uds;
  private final JwtRequestFilter jwtAuthenticationFilter;

  public WebSecurity(UserDetailsService uds, JwtRequestFilter jwtAuthenticationFilter) {
    this.uds = uds;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(uds);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults()) // <-- utilise CorsConfigurationSource

      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      .authorizeHttpRequests(auth -> auth
        // Laisse passer les préflights CORS
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // App Configuration API (public access for all)
        .requestMatchers(HttpMethod.GET, "/api/config/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/config/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/config/**").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/config/**").permitAll()
        .requestMatchers(HttpMethod.PATCH, "/api/config/**").permitAll()
        // Ouvre SSE + delta
        .requestMatchers("/events/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
        .requestMatchers("/api/patient/changes").permitAll()
        .requestMatchers("/api/consultation/**").permitAll()
        .requestMatchers("/api/documents/**").permitAll()
        .requestMatchers("/api/rdv/**").permitAll()
        // Auth publiques (login/register)
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/v1/**").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").permitAll()

        .requestMatchers(HttpMethod.GET, "/api/v1/encounters/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/encounters/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/v1/encounters/**").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/v1/encounters/**").permitAll()
        // (optionnel pour débug) ouvrir GET patients :
        .requestMatchers(HttpMethod.GET, "/api/patients/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/patients/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/patients/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/patients/**").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").permitAll()
        // le reste protégé par JWT
        .anyRequest().authenticated()
      )

      .authenticationProvider(authenticationProvider())
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
