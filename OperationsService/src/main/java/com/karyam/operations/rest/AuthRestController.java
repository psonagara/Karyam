package com.karyam.operations.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.dto.request.LoginRequest;
import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;
import com.karyam.operations.service.IAuthService;
import com.karyam.operations.util.CommonUtil;

@RestController
@RequestMapping(IMappingConstants.AUTH_API)
public class AuthRestController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthRestController.class);
	
	@Autowired
	private IAuthService authService;

	@PostMapping(IMappingConstants.REGISTER)
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		LOG.debug("Register Request Received: " + request);
		String message = authService.register(request);
		return CommonUtil.prepareResponseMessage(message, HttpStatus.CREATED);
	}

	@PostMapping(IMappingConstants.LOGIN)
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		LOG.debug("Login Request Received: " + request);
		LoginResponse response = authService.login(request);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
}
