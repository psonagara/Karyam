package com.karyam.notification.service;

import java.util.List;

import com.karyam.notification.dto.response.NotificationResponse;
import com.karyam.notification.entity.Notification;
import com.karyam.notification.entity.User;

public interface INotificationService {

	List<NotificationResponse> getNotifications();
	void saveNotification(Notification notification);
	User getUserById(Long userId);
}
