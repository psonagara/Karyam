package com.karyam.operations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.karyam.operations.enu.UserRole;
import com.karyam.operations.filter.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private SecurityFilter securityFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		String admin = UserRole.ADMIN.toString();
		String hr = UserRole.HR.toString();
		String accountant = UserRole.ACCOUNTANT.toString();
		String siteManager = UserRole.SITE_MANAGER.toString();
		
		httpSecurity
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/attendance/export", "/api/approvals/pending", "/api/approvals/{id}", "/api/payroll/export").hasAuthority(admin)
				.requestMatchers(HttpMethod.POST, "/api/projects", "/api/labors", "/api/attendance/mark", "/api/vendors", "/api/vendors/{id}/payment", "/api/expenses", "/api/approvals/{id}/approve", "/api/approvals/{id}/reject", "/api/payroll/generate/{projectId}", "/api/payroll/{id}/mark-paid", "/api/payroll/mark-all-paid").hasAuthority(admin)
				.requestMatchers(HttpMethod.PUT, "/api/projects/{id}", "/api/labors/{id}", "/api/vendors/{id}", "/api/expenses/{id}").hasAuthority(admin)
				.requestMatchers(HttpMethod.DELETE, "/api/projects/{id}", "/api/labors/{id}", "/api/vendors/{id}", "/api/expenses/{id}").hasAuthority(admin)
				.requestMatchers(HttpMethod.GET, "/api/attendance/export", "/api/payroll/export").hasAuthority(hr)
				.requestMatchers(HttpMethod.POST, "/api/labors", "/api/attendance/mark", "/api/payroll/generate/{projectId}", "/api/payroll/{id}/mark-paid", "/api/payroll/mark-all-paid").hasAuthority(hr)
				.requestMatchers(HttpMethod.PUT, "/api/labors/{id}").hasAuthority(hr)
				.requestMatchers(HttpMethod.DELETE, "/api/labors/{id}").hasAuthority(hr)
				.requestMatchers(HttpMethod.POST, "/api/vendors", "/api/vendors/{id}/payment", "/api/expenses").hasAuthority(accountant)
				.requestMatchers(HttpMethod.PUT, "/api/vendors/{id}", "/api/expenses/{id}").hasAuthority(accountant)
				.requestMatchers(HttpMethod.DELETE, "/api/vendors/{id}", "/api/expenses/{id}").hasAuthority(accountant)
				.requestMatchers(HttpMethod.GET, "/api/approvals/pending", "/api/approvals/{id}").hasAuthority(siteManager)
				.requestMatchers(HttpMethod.POST, "/api/expenses", "/api/approvals/{id}/approve", "/api/approvals/{id}/reject").hasAuthority(siteManager)
				.anyRequest().authenticated()
				)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.formLogin(form -> form.disable())
		.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) { 
		return config.getAuthenticationManager();
	}
}
