package com.karyam.operations.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

	T content;
	String message;
	String status;
	int statusCode;
	LocalDateTime timestamp;
}
