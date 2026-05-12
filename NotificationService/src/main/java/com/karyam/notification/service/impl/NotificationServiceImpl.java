package com.karyam.notification.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.karyam.notification.dto.response.NotificationResponse;
import com.karyam.notification.entity.Notification;
import com.karyam.notification.entity.User;
import com.karyam.notification.helper.NotificationMappingHelper;
import com.karyam.notification.repo.NotificationRepository;
import com.karyam.notification.repo.UserRepository;
import com.karyam.notification.service.INotificationService;
import com.karyam.notification.util.JwtUtil;

@Service
public class NotificationServiceImpl implements INotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Override
	public List<NotificationResponse> getNotifications() {
		Sort sort = Sort.by(Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(0, 5, sort);
		List<Notification> notificationList = notificationRepository.findByUserId(JwtUtil.getUserId(), pageable);
		List<NotificationResponse> response = notificationList.stream()
				.map(NotificationMappingHelper::toNotificationResponse)
				.collect(Collectors.toList());
		return response;
	}

	@Override
	public void saveNotification(Notification notification) {
		try {
			Notification savedNotification = notificationRepository.save(notification);
			sendNotification(savedNotification);
		} catch (Exception e) {
			throw new RuntimeException("Audit Log save failed");
		}
	}
	
	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).get();
	}
	
	private void sendNotification(Notification notification) {
		NotificationResponse notificationResponse = NotificationMappingHelper.toNotificationResponse(notification);
	    messagingTemplate.convertAndSendToUser(notificationResponse.getUserId().toString(), "/queue/notifications", notificationResponse);
	}
}
