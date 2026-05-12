package com.karyam.operations.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.repo.UserRepository.UserName;
import com.karyam.operations.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserName> getAllUsersName() {
		return userRepository.findAllUserBy();
	}
}
