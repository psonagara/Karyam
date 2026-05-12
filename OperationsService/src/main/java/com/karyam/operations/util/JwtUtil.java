package com.karyam.operations.util;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.karyam.operations.config.props.JwtProperties;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.dto.UserDetails;
import com.karyam.operations.enu.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Autowired
	private JwtProperties jwtProperties;
	
	public String generateToken(Long userId, String name, String email, UserRole role) {
		return Jwts.builder()
				.setSubject(email)
				.claim(ICommonConstants.USER_ID, userId)
				.claim(ICommonConstants.NAME, name)
				.claim(ICommonConstants.ROLE, role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(jwtProperties.getExpTimeInMin())))
				.signWith(Keys.hmacShaKeyFor(jwtProperties.getSecreteKey().getBytes()), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecreteKey().getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public static String getEmail() {
		return getUserDetails().getEmail();
	}

	public static String getName() {
		return getUserDetails().getName();
	}
	
	public static UserDetails getUserDetails() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
	
	public static Long getUserId() {
		return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	public static UserRole getRole() {
		@SuppressWarnings("unchecked")
		SimpleGrantedAuthority authority = ((List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities()).get(0);
		return UserRole.valueOf(authority.getAuthority());
	}
}
