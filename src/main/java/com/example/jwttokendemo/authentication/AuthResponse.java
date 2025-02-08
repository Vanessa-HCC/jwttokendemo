package com.example.jwttokendemo.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
	
	private String token;
	private int expiration;
	private String errorMessage;
	private boolean success;
	
	public AuthResponse(String token, int expiration) {
		this.token = token;
		this.expiration = expiration;
		this.success = true;
	}
	
	public AuthResponse(String errorMessage) {
		this.errorMessage = errorMessage;
		this.success = false;
	}
}
