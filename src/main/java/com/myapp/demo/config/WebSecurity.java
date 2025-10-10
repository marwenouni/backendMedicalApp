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

        // Ouvre SSE + delta
        .requestMatchers("/events/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
        .requestMatchers("/api/patient/changes").permitAll()

        // Auth publiques (login/register)
        .requestMatchers("/auth/**").permitAll()

        // (optionnel pour débug) ouvrir GET patients :
        .requestMatchers(HttpMethod.GET, "/api/patient/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/patient/**").permitAll()
        // le reste protégé par JWT
        .anyRequest().authenticated()
      )

      .authenticationProvider(authenticationProvider())
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
