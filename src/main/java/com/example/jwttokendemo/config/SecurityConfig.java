package com.example.jwttokendemo.config;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jwttokendemo.authentication.CustomUserDetailsService;

@Configuration
public class SecurityConfig{
	
	private Logger logger = Logger.getLogger(SecurityConfig.class.getName());
	
	private final JWTFilter jwtFilter;
	
	public SecurityConfig(CustomUserDetailsService customUserDetailsService, JWTFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf -> csrf.disable()); // 開發用
				
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.authorizeHttpRequests(auth -> 
			auth.requestMatchers("/api/user/public/login", "/login", "/home").permitAll()
				.anyRequest().authenticated());

		return http.build();
	}
	
	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
