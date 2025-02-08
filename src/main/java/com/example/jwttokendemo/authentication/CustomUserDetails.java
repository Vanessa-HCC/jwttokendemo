package com.example.jwttokendemo.authentication;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Integer userId;
	private String username;
	private String nickname;
	private String password;
	
    public CustomUserDetails(Integer userId, String username, String nickname, String password) {
    	this.userId = userId;
    	this.username = username;
    	this.nickname = nickname;
        this.password = password;
    }

	public Integer getUserId() {
		return this.userId;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

	public String getNickname() {
		return this.nickname;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
	}
    
}
