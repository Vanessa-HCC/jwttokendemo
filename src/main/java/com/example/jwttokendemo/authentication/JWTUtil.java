package com.example.jwttokendemo.authentication;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.jwttokendemo.config.JWTConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	private Logger logger = Logger.getLogger(JWTUtil.class.getName());

	private final JWTConfig jwtConfig;
	private SecretKey key;
	
	public JWTUtil(JWTConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
		this.key = getSecretkey();
	}
	
	private SecretKey getSecretkey() {
	    try {
	        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecretkey()));
	    } catch (IllegalArgumentException e) {
	        logger.info("secretkey encoded error: " + e.getMessage());
	        return null;
	    }
	}
	
	public String generateToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String userId = String.valueOf(userDetails.getUserId());
		String username = userDetails.getUsername();
		Date currentDate = new Date();
		
		String token = Jwts.builder()
				.claims(Map.of(
					"userId", userId,
					"username", username
				))
				.issuedAt(new Date())
				.expiration(new Date(currentDate.getTime() + jwtConfig.getExpiration()))
				.signWith(key)
				.compact();
		
		return token;
	}
	
	public Claims extractClaims(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(getSecretkey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
		return claims;
	}
	
	public Integer getUserIdFromJWT(String token) {
		return extractClaims(token).get("userId", Integer.class);
	}
	
	public String getUsernameFromJWT(String token) {
		return extractClaims(token).get("username", String.class);
	}
	
	public Date getExpirationFromJWT(String token) {
		return extractClaims(token).getExpiration();
	}
	
	public int getExpirationInSeconds() {
		return jwtConfig.getExpiration();
	}
	
	public boolean validateToken(String token) {
		try {
			
			SecretKey key = getSecretkey();
			
			Jwts.parser().verifyWith(key);
			return true;
			
		}catch(SecurityException | MalformedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid.");
        }
	}
}
