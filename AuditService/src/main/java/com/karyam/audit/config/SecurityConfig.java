package com.karyam.audit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.karyam.audit.enu.UserRole;
import com.karyam.audit.filter.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private SecurityFilter securityFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		String admin = UserRole.ADMIN.toString();
		httpSecurity
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.anyRequest().hasAuthority(admin)
				)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.formLogin(form -> form.disable())
		.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) { 
		return config.getAuthenticationManager();
	}
}
