package com.karyam.operations.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.request.LoginRequest;
import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;
import com.karyam.operations.entity.User;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.exception.AccessDeniedException;
import com.karyam.operations.exception.BadCredentialsException;
import com.karyam.operations.exception.ConflictException;
import com.karyam.operations.exception.DisabledAccountException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.AuthMappingHelper;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.service.IAuthService;
import com.karyam.operations.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public String register(RegisterRequest request) {
		String email = request.getEmail();
		if (userRepository.existsByEmail(email))
			throw new ConflictException(IExceptionConstants.USER_ALREADY_EXIST);
		
		User user = AuthMappingHelper.toUser(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		try {
			User savedUser = userRepository.save(user);
			log.info("User registered successfully with id:{}", savedUser.getId());
			return IResponseConstants.REGISTRATION_SUCCESS;
		} catch (Exception exception) {		
			log.error("User registration failed");
			throw new InternalServerException(IExceptionConstants.REGISTRATION_FAILED);
		}
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		String email = request.getEmail();
		if (!userRepository.existsByEmail(email))
			throw new ResourceNotFoundException(IExceptionConstants.USER_NOT_REGISTERED);
		
		User user = userRepository.findByEmail(email).get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException(IExceptionConstants.WRONG_PASSWORD);
		}
		if (!user.getRole().equals(request.getRole())) {
			throw new AccessDeniedException(IExceptionConstants.WRONG_ROLE);
		}
		if (user.getIsActive() == ActivationStatus.INACTIVE) {
			throw new DisabledAccountException(IExceptionConstants.INACTIVE_PROFILE);
		}
		
		log.info("User login successful with id: {}", user.getId());
		String token = jwtUtil.generateToken(user.getId(), user.getName(), user.getEmail(), user.getRole());
		LoginResponse response = AuthMappingHelper.toLoginResponse(user);
		response.setToken(token);
		return response;
	}

}
