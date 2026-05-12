package com.karyam.audit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.karyam.event.dto.ApprovalDTO;
import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.LaborDTO;
import com.karyam.event.dto.PayrollDTO;
import com.karyam.event.dto.ProjectDTO;
import com.karyam.event.dto.RecordDTO;
import com.karyam.event.dto.VendorDTO;
import com.karyam.event.dto.VendorPaymetDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaConsumer {

	@Autowired
	private KafkaEventHandler kafkaEventHandler;

	@KafkaListener(topics = "project-event-topic", groupId = "audit-service-group")
	public void consumeProjectEvents(KafkaEvent<ProjectDTO> event) {

		log.info("Received ProjectEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());

		switch (event.getEventType()) {
		case "project.created":
			kafkaEventHandler.handleProjectCreated(event);
			break;
		case "project.updated":
			kafkaEventHandler.handleProjectUpdated(event);
			break;
		case "project.deleted":
			kafkaEventHandler.handleProjectDeleted(event);
			break;
		default:
			log.warn("Unknown event type: {}", event.getEventType());

		}
	}

	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "labor-event-topic", groupId = "audit-service-group")
	public void consumeLaborEvents(KafkaEvent<?> event) {

		log.info("Received LaborEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());

		switch (event.getEventType()) {
		case "labor.created":
			kafkaEventHandler.handleLaborCreated((KafkaEvent<LaborDTO>) event);
			break;
		case "labor.updated":
			kafkaEventHandler.handleLaborUpdated((KafkaEvent<LaborDTO>) event);
			break;
		case "labor.deleted":
			kafkaEventHandler.handleLaborDeleted((KafkaEvent<LaborDTO>) event);
			break;
		case "payroll.generated":
			kafkaEventHandler.handlePayrollGenerated((KafkaEvent<List<PayrollDTO>>) event);
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
	
	@KafkaListener(topics = "attendance-event-topic", groupId = "audit-service-group")
	public void consumeAttendanceEvents(KafkaEvent<List<RecordDTO>> event) {

		log.info("Received AttendanceEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());
		if (event.getEventType().equals("attendance.mark")) {
			kafkaEventHandler.handleAttendaceMark(event);
		} else {
			log.warn("Unknown event type: {}", event.getEventType());
		}
	}

	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "vendor-event-topic", groupId = "audit-service-group")
	public void consumeVendorEvents(KafkaEvent<?> event) {
		
		log.info("Received VendorEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());
		switch (event.getEventType()) {
		case "vendor.created":
			kafkaEventHandler.handleVendorCreated((KafkaEvent<VendorDTO>) event);
			break;
		case "vendor.updated":
			kafkaEventHandler.handleVendorUpdated((KafkaEvent<VendorDTO>) event);
			break;
		case "vendor.deleted":
			kafkaEventHandler.handleVendorDeleted((KafkaEvent<VendorDTO>) event);
			break;
		case "vendor.payment.recorded":
			kafkaEventHandler.handleVendorPaymentEvent((KafkaEvent<VendorPaymetDTO>) event);
			break;
		default:
			log.warn("Unknown event type: {}", event.getEventType());
			
		}
	}
	
	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "expense-event-topic", groupId = "audit-service-group")
	public void consumeExpenseEvents(KafkaEvent<?> event) {
		
		log.info("Received ExpenseEvent, Event: {}, Id: {}", event.getEventType(), event.getEventId());
		switch (event.getEventType()) {
		case "expense.created":
			kafkaEventHandler.handleExpenseCreated((KafkaEvent<ExpenseDTO>) event);
			break;
		case "expense.updated":
			kafkaEventHandler.handleExpenseUpdated((KafkaEvent<ExpenseDTO>) event);
			break;
		case "expense.deleted":
			kafkaEventHandler.handleExpenseDeleted((KafkaEvent<ExpenseDTO>) event);
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
}
