package com.karyam.operations.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.ApprovalDTO;
import com.karyam.event.dto.KafkaEvent;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.response.ExpenseListResponse;
import com.karyam.operations.dto.response.ExpenseResponse;
import com.karyam.operations.entity.Expense;
import com.karyam.operations.enu.ExpenseStatus;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.ExpenseMappingHelper;
import com.karyam.operations.repo.ExpenseRepository;
import com.karyam.operations.service.IApprovalService;
import com.karyam.operations.service.IExpenseService;
import com.karyam.operations.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class ApprovalServiceIimpl implements IApprovalService {
	
	@Autowired
	private IExpenseService expenseService;
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Override
	public ExpenseListResponse getAllPendingApprovals(Map<String, Object> requestMap, Pageable pageable) {
		return expenseService.filterExpense(requestMap, pageable);
	}

	@Override
	public ExpenseResponse getApprovalById(Long approvalId) {
		Expense expense = expenseRepository.findByIdAndStatus(approvalId, ExpenseStatus.PENDING)
				.orElseThrow(() -> new ResourceNotFoundException());
		return ExpenseMappingHelper.toExpenseResponse(expense);
	}

	@Override
	public Map<String, Object> getApprovalStats() {
		Map<String, Object> response = new HashMap<>();
		response.put("pendingCount", expenseRepository.countByStatus(ExpenseStatus.PENDING));
		response.put("pendingAmount", expenseRepository.findTotalPendingAmount());
		response.put("approvedToday", expenseRepository.countByStatusAndDate(ExpenseStatus.APPROVED, LocalDate.now()));
		response.put("rejectedToday", expenseRepository.countByStatusAndDate(ExpenseStatus.REJECTED, LocalDate.now()));
		return response;
	}

	@Override
	@Transactional
	public String approveExpense(Long approvalId, RequestMetadata data) {
		expenseRepository.approveExpense(approvalId, JwtUtil.getUserId());
		
		ApprovalDTO approvalDTO = new ApprovalDTO(approvalId);
		KafkaEvent<?> approvalEvent = ExpenseMappingHelper.createApprovalEvent(data, "expense.approved", null, approvalDTO);
		kafkaProducer.publishExpenseEvent(approvalEvent);
		
		return IResponseConstants.EXPENSE_APPROVED_SUCESS;
	}

	@Override
	@Transactional
	public String rejectExpense(Long approvalId, Map<String, Object> requestMap, RequestMetadata data) {
		String reason = (String) requestMap.get(ICommonConstants.REJECTION_REASON);
		expenseRepository.rejectExpense(approvalId, JwtUtil.getUserId(), reason);
		
		ApprovalDTO approvalDTO = new ApprovalDTO(approvalId, reason);
		KafkaEvent<?> approvalEvent = ExpenseMappingHelper.createApprovalEvent(data, "expense.rejected", null, approvalDTO);
		kafkaProducer.publishExpenseEvent(approvalEvent);
		
		return IResponseConstants.EXPENSE_REJECTED_SUCESS;
	}
	
	
}
