package com.karyam.audit.dto.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private String message;
	private HttpStatus status;
	private LocalDateTime timestamp;
}
