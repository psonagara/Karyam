package com.karyam.operations.service;


import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.response.ExpenseListResponse;
import com.karyam.operations.dto.response.ExpenseResponse;

public interface IApprovalService {

	ExpenseListResponse getAllPendingApprovals(Map<String, Object> requestMap, Pageable pageable);
	ExpenseResponse getApprovalById(Long approvalId);
	Map<String, Object> getApprovalStats();
	String approveExpense(Long approvalId, RequestMetadata data);
	String rejectExpense(Long approvalId, Map<String, Object> requestMap, RequestMetadata data);
}
