package com.karyam.audit.dto.response;

import java.time.LocalDateTime;

import com.karyam.audit.enu.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditResponse {
	
	private Long id;
	private LocalDateTime timestamp;
	private String user;
	private Long userId;
	private UserRole userRole;
	private String action;
	private String entity;
	private Long entityId;
	private String details;
	private String oldValue;
	private String newValue;
	private String ipAddress;
	private String userAgent;
}
