package com.karyam.operations.filter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.dto.UserDetails;
import com.karyam.operations.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		LOG.debug("Entering SecurityFilter.doFilterInternal");

		String authorization = request.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Bearer ") || authorization.length() < 8) {
			filterChain.doFilter(request, response);
			return;
		} else {
			String token = authorization.substring(7);
			LOG.debug("Token: {}", token);
			try {
				Claims claims = jwtUtil.getClaims(token);
				String email = claims.getSubject();
				
				if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority((String) claims.get(ICommonConstants.ROLE));
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(claims.get(ICommonConstants.USER_ID), null, List.of(authority));
					usernamePasswordAuthenticationToken.setDetails(new UserDetails((String)claims.get(ICommonConstants.NAME), email));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					LOG.debug("Authentication set for user: {}", email);
				}
			} catch (ExpiredJwtException e) {
				sendResponse("TOKEN_EXPIRED", "JWT token has Expired", response);
				return;
			}  catch (MalformedJwtException e) {
				sendResponse("MALFORMED_TOKEN", "JWT token Altered", response);
				return;
			} catch (JwtException e) {
				sendResponse("INVALID_TOKEN", "Invalied JWT Token", response);
				return;
			} catch (Exception e) {
				sendResponse("AUTH_ERROR", "Authentication failed", response);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private void sendResponse(String code, String message, HttpServletResponse response) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		String body = """
				{
				"error": "%s",
				"message": "%s"
				}
				""".formatted(code, message);
		response.getWriter().write(body);
	}
}
