package com.example.jwttokendemo.user;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwttokendemo.authentication.AuthResponse;
import com.example.jwttokendemo.authentication.AuthService;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
	
	private final UserService userService;
	private final AuthService authService;
	
	public UserRestController(UserService userService, AuthService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@PostMapping("/public/login")
	public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> loginRequest) {
		String username = loginRequest.get("username");
	    String password = loginRequest.get("password");
	    
	    try {
	    	AuthResponse response = authService.authenticateAndGenerateToken(username, password);
	    	return ResponseEntity.ok(response);
	    }catch(AuthenticationException exception) {
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid username or password"));
	    }
	}
}
