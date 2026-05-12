package com.karyam.notification.helper;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.notification.dto.response.NotificationResponse;
import com.karyam.notification.entity.Notification;
import com.karyam.notification.entity.User;

public interface NotificationMappingHelper {

	public static NotificationResponse toNotificationResponse(Notification notification) {
		return NotificationResponse.builder()
				.id(notification.getId())
				.userId(notification.getUser().getId())
				.type(notification.getType())
				.title(notification.getTitle())
				.message(notification.getMessage())
				.relatedEntity(notification.getRelatedEntity())
				.relatedEntityId(notification.getRelatedEntityId())
				.isRead(notification.getIsRead())
				.createdAt(notification.getCreatedAt())
				.build();
	}
	
	public static Notification prepareNotification(KafkaEvent<?> event, User user, String title, String message, String entityId) {
		return Notification.builder()
				.eventId(event.getEventId())
				.user(user)
				.type(event.getEventType())
				.title(title)
				.message(message)
				.relatedEntity(event.getEntity())
				.relatedEntityId(entityId)
				.build();
	}
}
