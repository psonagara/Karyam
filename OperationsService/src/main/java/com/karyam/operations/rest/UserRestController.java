package com.karyam.operations.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.service.IUserService;

@RestController
@RequestMapping(IMappingConstants.USER_API)
public class UserRestController {
	
	@Autowired
	private IUserService userService;

	@GetMapping("names")
	public ResponseEntity<?> getUsersName() {
		return ResponseEntity.ok(userService.getAllUsersName());
	}
}
