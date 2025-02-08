package com.example.jwttokendemo.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jwttokendemo.user.User;
import com.example.jwttokendemo.user.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepo;
	
	public CustomUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findUserByUsername(username);
		
		if(user != null) {
			return new CustomUserDetails(user.getUserId(), user.getUsername(), user.getNickname(), user.getPassword());
		}else {
			throw new UsernameNotFoundException("User with username/email " + username + " not found");
		}
	}
	
}
