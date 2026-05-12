package com.karyam.notification.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
	
	private Long id;
	private Long userId;
	private String type;
	private String title;
	private String message;
	private String relatedEntity;
	private String relatedEntityId;
	private boolean isRead;
	private LocalDateTime createdAt;
}
