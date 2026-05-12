package com.karyam.operations.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
	
	List<UserName> findAllUserBy();
	
	public interface UserName {
		String getId();
		String getName();
	}
}
