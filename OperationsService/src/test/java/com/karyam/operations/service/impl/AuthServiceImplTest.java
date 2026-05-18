package com.karyam.operations.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.request.LoginRequest;
import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;
import com.karyam.operations.entity.User;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.UserRole;
import com.karyam.operations.exception.AccessDeniedException;
import com.karyam.operations.exception.BadCredentialsException;
import com.karyam.operations.exception.ConflictException;
import com.karyam.operations.exception.DisabledAccountException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.util.JwtUtil;
import com.karyam.operations.util.TestDataUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
	
	@InjectMocks
	private AuthServiceImpl authService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private JwtUtil jwtUtil;

	@Test
	void testRegister() {
		RegisterRequest request = TestDataUtil.getRegisterRequest();
		User user = TestDataUtil.getUser();
		
		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("$0a$40$ruc7G8uObXLYSOOsBnaRRuGqmBg/6DazAtoWDaKC9qU3PgWjHIBNu");
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// Success scenario
		String message = authService.register(request);
		assertEquals(IResponseConstants.REGISTRATION_SUCCESS, message);

		// Registration failed due to exception during saving User
		when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
		InternalServerException exception = assertThrows(InternalServerException.class, () -> authService.register(request));
		assertEquals(IExceptionConstants.REGISTRATION_FAILED, exception.getMessage());

		// Registration failed due to email already exists
		when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
		ConflictException conflictException = assertThrows(ConflictException.class, () -> authService.register(request));
		assertEquals(IExceptionConstants.USER_ALREADY_EXIST, conflictException.getMessage());
	}
	
	@Test
	void testLogin() { 
		LoginRequest request = TestDataUtil.getLoginRequest();
		User user = TestDataUtil.getUser();
		String email = request.getEmail();
		String token = TestDataUtil.getToken();
		
		when(userRepository.existsByEmail(email)).thenReturn(true);
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
		when(jwtUtil.generateToken(user.getId(), user.getName(), user.getEmail(), user.getRole())).thenReturn(token);
		
		// Login Success
		LoginResponse response = authService.login(request);
		assertEquals(token, response.getToken());
		assertEquals(user.getId(), response.getUserId());
		assertEquals(user.getEmail(), response.getEmail());
		assertEquals(user.getRole(), response.getRole());
		
		// user status is inactive
		user.setIsActive(ActivationStatus.INACTIVE);
		DisabledAccountException exception = assertThrows(DisabledAccountException.class, () -> authService.login(request));
		assertEquals(IExceptionConstants.INACTIVE_PROFILE, exception.getMessage());
		
		// invalid role
		request.setRole(UserRole.ACCOUNTANT);
		AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class, () -> authService.login(request));
		assertEquals(IExceptionConstants.WRONG_ROLE, accessDeniedException.getMessage());
		
		// invalid password
		request.setPassword("1234567890");
		when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);
		BadCredentialsException badCredentialsException = assertThrows(BadCredentialsException.class, () -> authService.login(request));
		assertEquals(IExceptionConstants.WRONG_PASSWORD, badCredentialsException.getMessage());		

		// user not exists with given email
		when(userRepository.existsByEmail(email)).thenReturn(false);
		ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
		assertEquals(IExceptionConstants.USER_NOT_REGISTERED, resourceNotFoundException.getMessage());		
	}

}
