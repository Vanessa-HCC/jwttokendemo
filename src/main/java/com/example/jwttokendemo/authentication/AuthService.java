package com.example.jwttokendemo.authentication;

import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private Logger logger = Logger.getLogger(AuthService.class.getName());
	
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	
	public AuthService(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}
	
	public AuthResponse authenticateAndGenerateToken(String username, String password) throws AuthenticationException {
		
		try {
			Authentication authentication = authenticateUser(username, password);
			String token = jwtUtil.generateToken(authentication);
			int expiration = jwtUtil.getExpirationInSeconds();
			return new AuthResponse(token, expiration);
		}catch(BadCredentialsException exception) {
            throw new BadCredentialsException("Invalid username or password", exception);
		}
	}
	
	private Authentication authenticateUser(String username, String password) {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
	
	public String getNicknameFromUserDetails(UserDetails userDetails) {
			if(userDetails instanceof CustomUserDetails) return ((CustomUserDetails)userDetails).getNickname();
			return "GUEST";
	}
}
