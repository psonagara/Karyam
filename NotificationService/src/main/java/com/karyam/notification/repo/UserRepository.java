package com.karyam.notification.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.notification.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
