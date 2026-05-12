package com.karyam.audit.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.karyam.audit.enu.ActivationStatus;
import com.karyam.audit.enu.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users", indexes = {
	    @Index(name = "idx_role", columnList = "role"),
	    @Index(name = "idx_status", columnList = "is_active")
	})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@Enumerated(EnumType.STRING)
	private ActivationStatus isActive = ActivationStatus.INACTIVE;
	
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}

