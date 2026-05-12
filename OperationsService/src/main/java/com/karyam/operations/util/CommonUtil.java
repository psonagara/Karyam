package com.karyam.operations.util;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.response.ApiResponse;
import com.karyam.operations.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface CommonUtil {

	public static ResponseEntity<ApiResponse<?>> prepareResponseMessage(String message, HttpStatus status) {
		return prepareResponse(message, status, null);
	}

	public static ResponseEntity<ApiResponse<?>> prepareResponseContent(Object content, HttpStatus status) {
		return prepareResponse(null, status, content);
	}

	public static ResponseEntity<ApiResponse<?>> prepareResponse(String message, HttpStatus status, Object content) {
	    ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message(message)
				.content(content)
				.status(status.toString())
				.statusCode(status.value())
				.timestamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<ApiResponse<?>>(apiResponse, status);
	}
	
	public static RequestMetadata getRequestMetadata(HttpServletRequest request) {
		RequestMetadata data = new RequestMetadata();
		data.setIpAddress(getClientIpAddress(request));
		data.setUserAgent(request.getHeader("User-Agent"));
		return data;
	}
	
	public static ResponseEntity<?> prepareErrorResponse(String message, HttpStatus status) {
		ErrorResponse response = ErrorResponse.builder()
				.message(message)
				.status(status)
				.timestamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<>(response, status);
	}

	private static String getClientIpAddress(HttpServletRequest request) {
		String[] headerNames = {
				"X-Forwarded-For",
				"Proxy-Client-IP",
				"WL-Proxy-Client-IP",
				"HTTP_X_FORWARDED_FOR",
				"HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP",
				"HTTP_CLIENT_IP",
				"HTTP_FORWARDED_FOR",
				"HTTP_FORWARDED",
				"HTTP_VIA",
				"REMOTE_ADDR"
		};
		for (String header : headerNames) {
			String ip = request.getHeader(header);
			if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
				return ip.split(",")[0].trim();
			}
		}
		return request.getRemoteAddr();
	}
	
	public static KafkaEvent<?> createKafkaEvent(RequestMetadata data, String eventType, String entity, Object oldValue, Object newValue) { 
		return KafkaEvent.builder()
				.eventId(UUID.randomUUID().toString())
				.eventType(eventType)
				.entity(entity)
				.timestamp(LocalDateTime.now())
				.oldValue(oldValue)
				.newValue(newValue)
				.userId(JwtUtil.getUserId())
				.userName(JwtUtil.getName())
				.userRole(JwtUtil.getRole())
				.ipAddress(data.getIpAddress())
				.userAgent(data.getUserAgent())
				.build();
	}
}
