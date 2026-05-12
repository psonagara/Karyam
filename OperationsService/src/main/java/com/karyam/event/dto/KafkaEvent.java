package com.karyam.event.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.karyam.operations.enu.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaEvent<T> {

	private String eventId;
	private String eventType;
	private String entity;
	private LocalDateTime timestamp;

	// This tells Jackson to use the actual class type of the object
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private T oldValue;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private T newValue;

	private Long userId;
	private String userName;
	private UserRole userRole;
	private String ipAddress;
	private String userAgent;
}
