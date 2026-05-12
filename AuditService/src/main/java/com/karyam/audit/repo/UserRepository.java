package com.karyam.audit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.audit.entity.User;
import com.karyam.audit.enu.ActivationStatus;

public interface UserRepository extends JpaRepository<User, Long> {

	long countByIsActive(ActivationStatus isActive);
}
