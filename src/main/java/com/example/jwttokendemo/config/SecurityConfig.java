package com.example.jwttokendemo.config;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jwttokendemo.authentication.CustomUserDetailsService;

@Configuration
public class SecurityConfig{
	
	private Logger logger = Logger.getLogger(SecurityConfig.class.getName());
	
	private final CustomUserDetailsService customUserDetailsService;
	private final JWTFilter jwtFilter;
	
	public SecurityConfig(CustomUserDetailsService customUserDetailsService, JWTFilter jwtFilter) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtFilter = jwtFilter;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf -> csrf.disable());
				
//		http.formLogin().disable();
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.authorizeHttpRequests(auth -> 
			auth.requestMatchers("/api/user/public/login", "/login", "/home").permitAll()
				.anyRequest().authenticated());

		
		http.sessionManagement(sessionManagement -> 
				sessionManagement.sessionConcurrency(sessionConcurrency ->
					sessionConcurrency.maximumSessions(1).expiredUrl("/home")));

		return http.build();
	}
	
	@Bean
	AuthenticationManager authManager(HttpSecurity http) throws Exception{
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService);
//			.passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}

}
