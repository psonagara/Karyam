package com.karyam.operations.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.ExpenseRequest;
import com.karyam.operations.dto.response.ExpenseListResponse;
import com.karyam.operations.dto.response.ExpenseResponse;

public interface IExpenseService {

	String createExpense(ExpenseRequest request, RequestMetadata data);
	ExpenseListResponse filterExpense(Map<String, Object> requestMap, Pageable pageable);
	ExpenseResponse getExpenseById(Long expenseId);
	String updateExpenseDetail(Long expenseId, ExpenseRequest request, RequestMetadata data);
	String deleteExpenseById(Long expenseId, RequestMetadata data);
	Map<String, Object> getExpenseStats();
}
