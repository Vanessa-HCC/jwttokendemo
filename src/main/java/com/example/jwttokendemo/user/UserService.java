package com.example.jwttokendemo.user;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private Logger logger = Logger.getLogger(UserService.class.getName());
	
	private final UserRepository userRepo;
	
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
}
