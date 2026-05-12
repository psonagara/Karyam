package com.karyam.audit.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.karyam.audit.dto.response.ErrorResponse;

public interface CommonUtil {

	public static ResponseEntity<?> prepareErrorResponse(String message, HttpStatus status) {
		ErrorResponse response = ErrorResponse.builder()
				.message(message)
				.status(status)
				.timestamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<>(response, status);
	}
}
