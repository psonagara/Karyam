package com.karyam.operations.service;

import com.karyam.operations.dto.request.LoginRequest;
import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;

public interface IAuthService {

	String register(RegisterRequest request);
	LoginResponse login(LoginRequest request);
}
