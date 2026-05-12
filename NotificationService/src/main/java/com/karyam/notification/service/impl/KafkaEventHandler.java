package com.karyam.notification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karyam.event.dto.ApprovalDTO;
import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.PayrollDTO;
import com.karyam.event.dto.PayrollPaidDTO;
import com.karyam.notification.entity.Notification;
import com.karyam.notification.entity.User;
import com.karyam.notification.helper.NotificationMappingHelper;
import com.karyam.notification.service.INotificationService;

@Service
public class KafkaEventHandler {
	
	@Autowired
	private INotificationService notificationService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public void handleExpenseEvent(KafkaEvent<ExpenseDTO> event) {
		ExpenseDTO expense = event.getNewValue();
		String title = "New Expense Created";
		String message = "New Expense Created of %s Category and ExpenseId is %s".formatted(expense.getCategory(), expense.getId());
		saveNotification(event, title, message, expense.getExpenseId());
	}

	public void handleApprovalApproved(KafkaEvent<ApprovalDTO> event) {
		Long id = event.getNewValue().getId();
		String title = "Expense Approved";
		String message = "Expense is Approved, id is %d".formatted(id);
		saveNotification(event, title, message, String.valueOf(id));
	}

	public void handleApprovalRejected(KafkaEvent<ApprovalDTO> event) {
		Long id = event.getNewValue().getId();
		String title = "Expense Rejected";
		String message = "Expense is Rejected because %s, id is %d".formatted(event.getNewValue().getRejectionReason(), id);
		saveNotification(event, title, message, String.valueOf(id));
	}
	

	public void handlePayrollGenerated(KafkaEvent<?> event) {
		List<PayrollDTO> payrolls = objectMapper.convertValue(
		        event.getNewValue(), 
		        new TypeReference<List<PayrollDTO>>() {}
		    );
		String title = "Payroll Generated";
		String message = "Payroll Generated with total %d records".formatted(payrolls.size());
		saveNotification(event, title, message, null);
	}

	public void handlePayrollPaid(KafkaEvent<?> event) {
		PayrollPaidDTO paidDTO = objectMapper.convertValue(
		        event.getNewValue(), 
		        new TypeReference<PayrollPaidDTO>() {}
		    );
		String title = "Payroll Paid";
		String message = "Payroll of id %d marked Paid, Payment method is %s".formatted(paidDTO.getId(), paidDTO.getPaymentMethod());
		saveNotification(event, title, message, String.valueOf(paidDTO.getId()));
	}

	public void handlePayrollPaidAll(KafkaEvent<?> event) {
		PayrollPaidDTO paidDTO = objectMapper.convertValue(
				event.getNewValue(), 
				new TypeReference<PayrollPaidDTO>() {}
				);
		String title = "Payroll Paid All";
		String message = "Payroll Paid for Project of id %d Payment method is %s".formatted(paidDTO.getId(), paidDTO.getPaymentMethod());
		saveNotification(event, title, message, String.valueOf(paidDTO.getId()));
	}

	private void saveNotification(KafkaEvent<?> event, String title, String message, String entityId) {
		User user = notificationService.getUserById(event.getUserId());
		Notification notification = NotificationMappingHelper.prepareNotification(event, user, title, message, entityId);
		notificationService.saveNotification(notification);
	}

}
