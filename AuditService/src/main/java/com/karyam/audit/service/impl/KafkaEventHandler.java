package com.karyam.audit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karyam.audit.entity.AuditLog;
import com.karyam.audit.entity.User;
import com.karyam.audit.helper.AuditMappingHelper;
import com.karyam.audit.helper.MapHelper;
import com.karyam.audit.service.IAuditService;
import com.karyam.event.dto.ApprovalDTO;
import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.LaborDTO;
import com.karyam.event.dto.PayrollDTO;
import com.karyam.event.dto.PayrollPaidDTO;
import com.karyam.event.dto.ProjectDTO;
import com.karyam.event.dto.RecordDTO;
import com.karyam.event.dto.VendorDTO;
import com.karyam.event.dto.VendorPaymetDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaEventHandler {

	@Autowired
	private IAuditService auditService;

	@Autowired
	private ObjectMapper objectMapper;

	public void handleProjectCreated(KafkaEvent<ProjectDTO> event) {
		try {
			String details = String.format("Created project %s: %s", event.getNewValue().getProjectDisplayId(), event.getNewValue().getProjectName());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getProjectId(), "CREATE", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for project.created event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleProjectUpdated(KafkaEvent<ProjectDTO> event) {
		try {
			String details = String.format("Updated project %s", event.getNewValue().getProjectDisplayId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getProjectId(), "UPDATE", details, oldValueJson, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for project.updated event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleProjectDeleted(KafkaEvent<ProjectDTO> event) {
		try {
			String details = String.format("Project %s deleted", event.getOldValue().getProjectDisplayId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			handleKafkaEvent(event, event.getOldValue().getProjectId(), "DELETE", details, oldValueJson, null);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for project.deleted event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleLaborCreated(KafkaEvent<LaborDTO> event) {
		try {
			String details = String.format("Created labor %s: %s", event.getNewValue().getLaborDisplayId(), event.getNewValue().getName());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getLaborId(), "CREATE", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for labor.created event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleLaborUpdated(KafkaEvent<LaborDTO> event) {
		try {
			String details = String.format("Updated labor %s", event.getNewValue().getLaborDisplayId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getLaborId(), "UPDATE", details, oldValueJson, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for labor.updated event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleLaborDeleted(KafkaEvent<LaborDTO> event) {
		try {
			String details = String.format("Labor %s deleted", event.getOldValue().getLaborDisplayId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			handleKafkaEvent(event, event.getOldValue().getLaborId(), "DELETE", details, oldValueJson, null);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for labor.deleted event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleAttendaceMark(KafkaEvent<List<RecordDTO>> event) {
		log.info("Processing {} event", event.getEventType());
		try {
			User user = auditService.getUserById(event.getUserId());
			List<RecordDTO> records = objectMapper.convertValue(
					event.getNewValue(), 
					new TypeReference<List<RecordDTO>>() {}
					);
			List<AuditLog> auditLogs = new ArrayList<>();
			for (RecordDTO record : records) {
				AuditLog auditLog = handleAttendanceMark(event, record, user);
				auditLog.setEventId(auditLog.getEventId() + record.getId());
				auditLogs.add(auditLog);
			}
			auditService.saveAllAuditLogs(auditLogs);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Audit log created for event: {}", event.getEventType());
	}

	public void handleVendorCreated(KafkaEvent<VendorDTO> event) {
		try {
			String details = String.format("Created Vendor %s with Id %d", event.getNewValue().getName(), event.getNewValue().getId());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "CREATE", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for vendor.created event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleVendorUpdated(KafkaEvent<VendorDTO> event) {
		try {
			String details = String.format("Updated Vendor %s, Id: %d", event.getOldValue().getName(), event.getNewValue().getId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "UPDATE", details, oldValueJson, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for vendor.updated event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleVendorDeleted(KafkaEvent<VendorDTO> event) {
		try {
			String details = String.format("Vendor %s with Id: %d deleted", event.getOldValue().getName(), event.getOldValue().getId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			handleKafkaEvent(event, event.getOldValue().getId(), "DELETE", details, oldValueJson, null);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for vendor.deleted event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleVendorPaymentEvent(KafkaEvent<VendorPaymetDTO> event) {
		try {
			String details = String.format("Payment made, paymentId: %s to Vendor, Id: %d", event.getNewValue().getPaymentId(), event.getNewValue().getVendorId());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "CREATE", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for vendor.payment.recorded event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleExpenseCreated(KafkaEvent<ExpenseDTO> event) {
		try {
			String details = String.format("Created Expense %s & project Id %d", event.getNewValue().getExpenseId(), event.getNewValue().getProjectId());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "CREATE", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for expense.created event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleExpenseUpdated(KafkaEvent<ExpenseDTO> event) {
		try {
			String details = String.format("Updated Expense %s", event.getOldValue().getExpenseId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "UPDATE", details, oldValueJson, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for expense.updated event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleExpenseDeleted(KafkaEvent<ExpenseDTO> event) {
		try {
			String details = String.format("Expense %s deleted", event.getOldValue().getExpenseId());
			String oldValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getOldValue()));
			handleKafkaEvent(event, event.getOldValue().getId(), "DELETE", details, oldValueJson, null);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for expense.deleted event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	private AuditLog handleAttendanceMark(KafkaEvent<List<RecordDTO>> event, RecordDTO record, User user) throws JsonProcessingException {
		String details = String.format("Attendance marked %s for laborId %s", record.getStatus(),record.getLaborId());
		String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(record));
		return AuditMappingHelper.prepareAuditLog(event, user, record.getId(), "ATTENDANCE_MARK", details, null, newValueJson);
	}

	private void handleKafkaEvent(KafkaEvent<?> event, Long id, String action,
			String details, String oldValueJson, String newValueJson) {

		log.info("Processing {} event for entity id: {}", event.getEventType(), id);
		User user = auditService.getUserById(event.getUserId());
		AuditLog auditLog = AuditMappingHelper.prepareAuditLog(event, user, id, action, details, oldValueJson, newValueJson);
		auditService.saveAuditLog(auditLog);
		log.info("Audit log created for event: {} & entity id: {}", event.getEventType(), id);
	}

	public void handleApprovalApproved(KafkaEvent<ApprovalDTO> event) {
		try {
			String details = String.format("Expense Approved, id: %d", event.getNewValue().getId());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "APPROVED", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for expense.approved event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handleApprovalRejected(KafkaEvent<ApprovalDTO> event) {
		try {
			String details = String.format("Expense Rejected, id: %d", event.getNewValue().getId());
			String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(event.getNewValue()));
			handleKafkaEvent(event, event.getNewValue().getId(), "REJECTED", details, null, newValueJson);
		} catch (JsonProcessingException e) {
			log.error("Error creating audit log for expense.rejected event", e);
			throw new RuntimeException("Failed to create audit log", e);
		}
	}

	public void handlePayrollGenerated(KafkaEvent<List<PayrollDTO>> event) {
		log.info("Processing {} event", event.getEventType());
		try {
			User user = auditService.getUserById(event.getUserId());
			List<PayrollDTO> payrolls = objectMapper.convertValue(
					event.getNewValue(), 
					new TypeReference<List<PayrollDTO>>() {}
					);
			List<AuditLog> auditLogs = new ArrayList<>();
			for (PayrollDTO payroll : payrolls) {
				AuditLog auditLog = handlePayrollGenerated(event, payroll, user);
				auditLog.setEventId(auditLog.getEventId() + payroll.getId());
				auditLogs.add(auditLog);
			}
			auditService.saveAllAuditLogs(auditLogs);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Audit log created for event: {}", event.getEventType());
	}

	private AuditLog handlePayrollGenerated(KafkaEvent<List<PayrollDTO>> event, PayrollDTO payroll, User user) throws JsonProcessingException {
		String details = String.format("Payroll Generated with id %s for laborId %s", payroll.getPayrollId(), payroll.getLaborId());
		String newValueJson = objectMapper.writeValueAsString(MapHelper.createValueMap(payroll));
		return AuditMappingHelper.prepareAuditLog(event, user, payroll.getId(), "PAYROLL_GENERATED", details, null, newValueJson);
	}

	public void handlePayrollPaid(KafkaEvent<?> event) {
		PayrollPaidDTO paidDTO = objectMapper.convertValue(
				event.getNewValue(), 
				new TypeReference<PayrollPaidDTO>() {}
				);
		String details = String.format("Payroll Mark Paid, id: %d and Payment method is %s", paidDTO.getId(), paidDTO.getPaymentMethod());
		handleKafkaEvent(event, paidDTO.getId(), "PAYROLL_PAID", details, null, null);
	}

	public void handlePayrollPaidAll(KafkaEvent<?> event) {
		PayrollPaidDTO paidDTO = objectMapper.convertValue(
				event.getNewValue(), 
				new TypeReference<PayrollPaidDTO>() {}
				);
		String details = String.format("Payroll Mark Paid Bulk with project of id: %d and Payment method is %s", paidDTO.getId(), paidDTO.getPaymentMethod());
		handleKafkaEvent(event, paidDTO.getId(), "PAYROLL_PAID", details, null, null);
	}
}
