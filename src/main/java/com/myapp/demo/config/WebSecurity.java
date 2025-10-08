package com.myapp.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalAuthentication
public class WebSecurity {
	
	@Autowired
	private UserDetailsService uds;
	@Autowired
	private  JwtRequestFilter jwtAuthenticationFilter;
	private  AuthenticationConfiguration authenticationConfiguration;
	
//	 @Autowired
//	    public WebSecurity(AuthenticationConfiguration configuration) {
//	        this.authenticationConfiguration = configuration;
//	    }
	 
//	 @Bean
//	    public AuthenticationManager authenticationManager() throws Exception {
//	        return authenticationConfiguration.getAuthenticationManager();
//	    }
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    CorsFilter corsFilter() {
        CorsFilter filter = new CorsFilter();
        return filter;
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
		
		return http.addFilterBefore(corsFilter(), SessionManagementFilter.class).csrf().disable()
				.authorizeHttpRequests()
				//.requestMatchers("/api/ui").authenticated()
				.requestMatchers("/auth/**").permitAll()
				//.requestMatchers(HttpMethod.DELETE).hasAuthority("ADMIN") 	// If UserDetails.getAuthorities return [ADMIN, ...]
				//.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")		// If UserDetails.getAuthorities return [ROLE_ADMIN, ...] 
				.anyRequest().permitAll()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
		
		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(uds);
		authenticationProvider.setPasswordEncoder( passwordEncoder());
		return authenticationProvider;
	}
	
	 
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
	
	
	 
}