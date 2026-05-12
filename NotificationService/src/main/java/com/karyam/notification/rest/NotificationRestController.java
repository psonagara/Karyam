package com.karyam.notification.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.notification.constant.IMappingConstants;
import com.karyam.notification.dto.response.NotificationResponse;
import com.karyam.notification.service.INotificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.NOTIFICATION_API)
@Slf4j
public class NotificationRestController {
	
	@Autowired
	private INotificationService notificationService;

	@GetMapping
	public ResponseEntity<?> getNotifications() { 
		log.debug("Enter in NotificationRestController.getNotifications");
		List<NotificationResponse> notifications = notificationService.getNotifications();
		return ResponseEntity.ok(notifications);
	}
}
