package com.karyam.operations.helper;

import java.time.LocalDateTime;

import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;
import com.karyam.operations.entity.User;

public interface AuthMappingHelper {

	public static User toUser(RegisterRequest request) {
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setRole(request.getRole());
		user.setCreatedAt(LocalDateTime.now());
		return user;
	}
	
	public static LoginResponse toLoginResponse(User user) {
		LoginResponse response = new LoginResponse();
		response.setEmail(user.getEmail());
		response.setName(user.getName());
		response.setRole(user.getRole());
		response.setUserId(user.getId());
		return response;
	}
}
