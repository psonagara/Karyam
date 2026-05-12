package com.karyam.operations.helper;

import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.KafkaEvent;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.ExpenseRequest;
import com.karyam.operations.dto.response.ExpenseResponse;
import com.karyam.operations.entity.Expense;
import com.karyam.operations.util.CommonUtil;

public interface ExpenseMappingHelper {

	public static Expense toExpense(ExpenseRequest request) {
		return Expense.builder()
				.category(request.getCategory())
				.amount(request.getAmount())
				.date(request.getDate())
				.description(request.getDescription())
				.billNumber(request.getBillNumber())
				.build();
	}

	public static ExpenseResponse toExpenseResponse(Expense expense) {
		return ExpenseResponse.builder()
				.id(expense.getId())
				.expenseId(expense.getExpenseId())
				.projectId(expense.getProject().getId())
				.projectName(expense.getProject().getName())
				.vendorId(expense.getVendor() != null ? expense.getVendor().getId() : null)
				.vendorName(expense.getVendor() != null ? expense.getVendor().getName() : null)
				.category(expense.getCategory())
				.amount(expense.getAmount())
				.date(expense.getDate())
				.description(expense.getDescription())
				.billNumber(expense.getBillNumber())
				.status(expense.getStatus())
				.createdBy(expense.getCreatedBy() != null ? expense.getCreatedBy().getId() : null)
				.createdByName(expense.getCreatedBy() != null ? expense.getCreatedBy().getName() : null)
				.approvedBy(expense.getApprovedBy() != null ? expense.getApprovedBy().getId() : null)
				.approvedByName(expense.getApprovedBy() != null ? expense.getApprovedBy().getName() : null)
				.rejectedBy(expense.getRejectedBy() != null ? expense.getRejectedBy().getId() : null)
				.rejectionReason(expense.getRejectionReason())
				.createdAt(expense.getCreatedAt())
				.updatedAt(expense.getUpdatedAt())
				.build();
	}

	public static void updateExpense(ExpenseRequest request, Expense expense) {
		expense.setCategory(request.getCategory());
		expense.setAmount(request.getAmount());
		expense.setDate(request.getDate());
		expense.setDescription(request.getDescription());
		expense.setBillNumber(request.getBillNumber());
	}

	public static ExpenseDTO toExpenseDTO(Expense expense) {
		return ExpenseDTO.builder()
				.id(expense.getId())
				.expenseId(expense.getExpenseId())
				.projectId(expense.getProject().getId())
				.vendorId(expense.getVendor() != null ? expense.getVendor().getId() : null)
				.category(expense.getCategory())
				.amount(expense.getAmount())
				.date(expense.getDate())
				.description(expense.getDescription())
				.billNumber(expense.getBillNumber())
				.status(expense.getStatus())
				.createdBy(expense.getCreatedBy() != null ? expense.getCreatedBy().getId() : null)
				.approvedBy(expense.getApprovedBy() != null ? expense.getApprovedBy().getId() : null)
				.approvedAt(expense.getApprovedAt())
				.rejectedBy(expense.getRejectedBy() != null ? expense.getRejectedBy().getId() : null)
				.rejectedAt(expense.getRejectedAt())
				.rejectionReason(expense.getRejectionReason())
				.build();
	}
	
	public static KafkaEvent<?> createExpenseEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Expense", oldValue, newValue);
	}

	public static KafkaEvent<?> createApprovalEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Approval", oldValue, newValue);
	}
}
