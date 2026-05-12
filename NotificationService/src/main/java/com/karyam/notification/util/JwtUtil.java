package com.karyam.notification.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.karyam.notification.config.props.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Autowired
	private JwtProperties jwtProperties;
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecreteKey().getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public static Long getUserId() {
		return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
