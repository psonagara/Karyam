package com.karyam.operations.dto.request;

import com.karyam.operations.enu.UserRole;

import lombok.Data;

@Data
public class RegisterRequest {

	private String name;
	private String email;
	private String password;
	private UserRole role;
}
