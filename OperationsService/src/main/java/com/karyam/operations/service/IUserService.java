package com.karyam.operations.service;

import java.util.List;

import com.karyam.operations.repo.UserRepository.UserName;

public interface IUserService {

	List<UserName> getAllUsersName();
}
