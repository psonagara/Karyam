package com.karyam.operations.util;

import java.time.LocalDateTime;

import com.karyam.operations.dto.request.LoginRequest;
import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;
import com.karyam.operations.entity.User;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.UserRole;

public interface TestDataUtil {

	public static String getUserEmail() {
		return "user@karyam.com";
	}

	public static String getToken() {
		return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBrYXJ5YW0uaW4iLCJ1c2VySWQiOjEsIm5hbWUiOiJKb2huIERvZSIsIlJvbGUiOiJBRE1JTiIsImlhdCI6MTc3ODkwOTI0OSwiZXhwIjoxNzc4OTExOTQ5fQ.JLrtAIMP0i4guall-VwWUGVXG4uL_W2jE7NTTnfa_u4";
	}
	
	public static RegisterRequest getRegisterRequest() {
		RegisterRequest request = new RegisterRequest();
		request.setName("User Karyam");
		request.setEmail(getUserEmail());
		request.setPassword("12345678");
		request.setRole(UserRole.ADMIN);
		return request;
	}
	
	public static User getUser() {
		return User.builder()
				.id(1L)
				.name("User Karyam")
				.email(getUserEmail())
				.password("$0a$40$ruc7G8uObXLYSOOsBnaRRuGqmBg/6DazAtoWDaKC9qU3PgWjHIBNu")
				.role(UserRole.ADMIN)
				.createdAt(LocalDateTime.now())
				.isActive(ActivationStatus.ACTIVE)
				.build();
	}
	
	public static LoginRequest getLoginRequest() {
		LoginRequest request = new LoginRequest();
		request.setEmail(getUserEmail());
		request.setPassword("12345678");
		request.setRole(UserRole.ADMIN);
		return request;
	}
	
	public static LoginResponse getLoginResponse() {
		LoginResponse response = new LoginResponse();
		response.setToken(getToken());
		response.setUserId(1L);
		response.setName("User Karyam");
		response.setEmail(getUserEmail());
		response.setRole(UserRole.ADMIN);
		return response;
	}
}
