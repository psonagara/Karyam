package com.karyam.notification.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.ApprovalDTO;
import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.KafkaEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {
	
	@Autowired
	private KafkaEventHandler kafkaEventHandler;

	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "expense-event-topic", groupId = "notification-service-group")
	public void consumeExpenseEvents(KafkaEvent<?> event) {
		
		log.info("Received ExpenseEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());
		switch (event.getEventType()) {
		case "expense.created":
			kafkaEventHandler.handleExpenseEvent((KafkaEvent<ExpenseDTO>) event);
			break;
		case "expense.approved":
			kafkaEventHandler.handleApprovalApproved((KafkaEvent<ApprovalDTO>) event);
			break;
		case "expense.rejected":
			kafkaEventHandler.handleApprovalRejected((KafkaEvent<ApprovalDTO>) event);
			break;
		default:
			log.warn("Unknown event type: {}", event.getEventType());
			
		}
	}
	
	@KafkaListener(topics = "labor-event-topic", groupId = "notification-service-group")
	public void consumePayrollEvents(KafkaEvent<?> event) {
		
		log.info("Received PayrollEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());
		switch (event.getEventType()) {
		case "payroll.generated":
			kafkaEventHandler.handlePayrollGenerated(event);
			break;
		case "payroll.paid":
			kafkaEventHandler.handlePayrollPaid(event);
			break;
		case "payroll.paid.all":
			kafkaEventHandler.handlePayrollPaidAll(event);
			break;
		default:
			log.warn("Unknown event type: {}", event.getEventType());
			
		}
	}
}
