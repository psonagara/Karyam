package com.karyam.operations.dto.response;

import com.karyam.operations.enu.UserRole;

import lombok.Data;

@Data
public class LoginResponse {

	private String token;
	private Long userId;
	private String name;
	private String email;
	private UserRole role;
}
