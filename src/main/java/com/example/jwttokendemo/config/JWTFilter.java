package com.example.jwttokendemo.config;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwttokendemo.authentication.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {
	
	private Logger logger = Logger.getLogger(JWTFilter.class.getName());
	
	private final JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;
	private static final long REFRESH_THRESHOLD = 5 * 60 * 1000;
	
	public JWTFilter(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		logger.info("Request URI: " + requestURI);
		
		if(requestURI.startsWith("/home") || requestURI.startsWith("/login") || requestURI.startsWith("/api/user/public/login")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = "";
		if(requestURI.startsWith("/api")) {
			token = request.getHeader("token");
		}else {
			token = getTokenFromCookies(request);
		}
		
		if(token != null && jwtUtil.validateToken(token)) {
			
			String username = jwtUtil.getUsernameFromJWT(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			setAuthentication(userDetails, request);
			refreshTokenIdNeeded(token, response);
		}else{
		    SecurityContextHolder.clearContext();
		}
		
		filterChain.doFilter(request, response);
	}
	
	private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private void refreshTokenIdNeeded(String token, HttpServletResponse response) {
		long expirationTime  = jwtUtil.getExpirationFromJWT(token).getTime();
		long currentTime = System.currentTimeMillis();
		long remainingTime = expirationTime - currentTime;
		logger.info("remaining time of the token: " + remainingTime);
		
		if(remainingTime < REFRESH_THRESHOLD) {
			String newToken = jwtUtil.generateToken(SecurityContextHolder.getContext().getAuthentication());
			response.setHeader("token", newToken);
		}
	}
	
	private String getTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("token")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
