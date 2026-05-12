package com.karyam.audit.helper;

import java.time.LocalDateTime;

import com.karyam.audit.dto.response.AuditResponse;
import com.karyam.audit.entity.AuditLog;
import com.karyam.audit.entity.User;
import com.karyam.event.dto.KafkaEvent;

public interface AuditMappingHelper {

	public static AuditLog prepareAuditLog(KafkaEvent<?> event, User user, Long id, String action,
			String details, String oldValueJson, String newValueJson) {
		return AuditLog.builder()
				.eventId(event.getEventId())
				.timestamp(event.getTimestamp() != null ? event.getTimestamp() : LocalDateTime.now())
				.user(user)
				.userName(event.getUserName())
				.userRole(event.getUserRole())
				.action(action)
				.entity(event.getEntity())
				.entityId(id)
				.details(details)
				.oldValue(oldValueJson)
				.newValue(newValueJson)
				.ipAddress(event.getIpAddress())
				.userAgent(event.getUserAgent())
				.build();
	}
	
	public static AuditResponse toAuditResponse(AuditLog auditLog) {
		return AuditResponse.builder()
				.id(auditLog.getId())
				.timestamp(auditLog.getTimestamp())
				.user(auditLog.getUserName())
				.userId(auditLog.getUser().getId())
				.userRole(auditLog.getUserRole())
				.action(auditLog.getAction())
				.entity(auditLog.getEntity())
				.entityId(auditLog.getEntityId())
				.details(auditLog.getDetails())
				.oldValue(auditLog.getOldValue())
				.newValue(auditLog.getNewValue())
				.ipAddress(auditLog.getIpAddress())
				.userAgent(auditLog.getUserAgent())
				.build();
	}
}
