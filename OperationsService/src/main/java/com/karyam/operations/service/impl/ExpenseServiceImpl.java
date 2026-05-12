package com.karyam.operations.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.KafkaEvent;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.ExpenseRequest;
import com.karyam.operations.dto.response.ExpenseListResponse;
import com.karyam.operations.dto.response.ExpenseResponse;
import com.karyam.operations.entity.Expense;
import com.karyam.operations.entity.Project;
import com.karyam.operations.entity.User;
import com.karyam.operations.entity.Vendor;
import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ExpenseStatus;
import com.karyam.operations.exception.BadRequestException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.ExpenseMappingHelper;
import com.karyam.operations.repo.ExpenseRepository;
import com.karyam.operations.repo.ProjectRepository;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.repo.VendorRepository;
import com.karyam.operations.service.IExpenseService;
import com.karyam.operations.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseServiceImpl implements IExpenseService {
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;

	@Override
	public String createExpense(ExpenseRequest request, RequestMetadata data) {
		Long projectId = request.getProjectId();
		Long vendorId = request.getVendorId();
		
		Expense expense = ExpenseMappingHelper.toExpense(request);
		if (vendorId != null) {
			Vendor vendor = vendorRepository.findById(vendorId)
					.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.VENDOR_NOT_FOUND));
			expense.setVendor(vendor);
		}
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND));
		User user = userRepository.findById(JwtUtil.getUserId()).get();
		expense.setProject(project);
		expense.setCreatedBy(user);
		try {
			Expense savedExpense = expenseRepository.save(expense);
			log.info("Expense Created with id {}", savedExpense.getId());
			
			// publish event to kafka
			ExpenseDTO newValue = ExpenseMappingHelper.toExpenseDTO(savedExpense);
			KafkaEvent<?> expenseEvent = ExpenseMappingHelper.createExpenseEvent(data, "expense.created", null, newValue);
			kafkaProducer.publishExpenseEvent(expenseEvent);
		} catch (Exception exception) {
			log.error("Expense Creation failed", exception);
			throw new InternalServerException(IExceptionConstants.EXPENSE_CREATION_FAIL);
		}
		return IResponseConstants.EXPENSE_CREATION_SUCESS;
	}

	@Override
	public ExpenseListResponse filterExpense(Map<String, Object> requestMap, Pageable pageable) {
		Page<Expense> pages = expenseRepository.filterExpenses((ExpenseCategory) requestMap.get(ICommonConstants.CATEGORY),
				(String) requestMap.get(ICommonConstants.SEARCH), 
				(Long) requestMap.get(ICommonConstants.PROJECT_ID), 
				(ExpenseStatus) requestMap.get(ICommonConstants.STATUS),
				pageable);
		
		List<Expense> expenseList = pages.getContent();
		List<ExpenseResponse> expenseResponseList = expenseList.stream()
				.map(ExpenseMappingHelper::toExpenseResponse)
				.collect(Collectors.toList());
		ExpenseListResponse response = new ExpenseListResponse();
		response.setExpenses(expenseResponseList);
		response.setTotalPages(pages.getTotalPages());
		response.setNumber(pages.getNumber());
		return response;
	}

	@Override
	public ExpenseResponse getExpenseById(Long expenseId) {
		Optional<Expense> optional = expenseRepository.findById(expenseId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.EXPENSE_NOT_FOUND);
		
		return ExpenseMappingHelper.toExpenseResponse(optional.get());
	}

	@Override
	public String updateExpenseDetail(Long expenseId, ExpenseRequest request, RequestMetadata data) {
		Optional<Expense> optional = expenseRepository.findById(expenseId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.EXPENSE_NOT_FOUND);
		
		Expense expense = optional.get();
		if (!expense.getStatus().equals(ExpenseStatus.PENDING)) {
			throw new BadRequestException(IExceptionConstants.EXPENSE_INVALID_UPDATE);
		}
		
		Long projectId = request.getProjectId();
		Long vendorId = request.getVendorId();
		ExpenseDTO oldValue = ExpenseMappingHelper.toExpenseDTO(expense);
		if (vendorId != null) {
			Vendor vendor = vendorRepository.findById(vendorId)
					.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.VENDOR_NOT_FOUND));
			expense.setVendor(vendor);
		}
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND));
		expense.setProject(project);
		ExpenseMappingHelper.updateExpense(request, expense);
		try {
			Expense updatedExpense = expenseRepository.save(expense);
			log.info("Expense Updated with id {}", updatedExpense.getId());
			
			// publish event to kafka
			ExpenseDTO updatedValue = ExpenseMappingHelper.toExpenseDTO(updatedExpense);
			KafkaEvent<?> expenseEvent = ExpenseMappingHelper.createExpenseEvent(data, "expense.updated", oldValue, updatedValue);
			kafkaProducer.publishExpenseEvent(expenseEvent);
		} catch (Exception exception) {
			log.error("Expense Updation failed, Expense id: {}", expense.getId(), exception);
			throw new InternalServerException(IExceptionConstants.EXPENSE_UPDATE_FAIL);
		}
		return IResponseConstants.EXPENSE_UPDATE_SUCESS;
	}

	@Override
	public String deleteExpenseById(Long expenseId, RequestMetadata data) {
		Optional<Expense> optional = expenseRepository.findById(expenseId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.EXPENSE_NOT_FOUND);
		
		try {
			ExpenseDTO oldValue = ExpenseMappingHelper.toExpenseDTO(optional.get());
			expenseRepository.deleteById(expenseId);
			
			// publish event to kafka
			KafkaEvent<?> expenseEvent = ExpenseMappingHelper.createExpenseEvent(data, "expense.deleted", oldValue, null);
			kafkaProducer.publishExpenseEvent(expenseEvent);
		} catch (Exception exception) {
			log.error("Expense Deletion failed, Expense id: {}", expenseId, exception);
			throw new InternalServerException(IExceptionConstants.EXPENSE_UPDATE_FAIL);
		}
		return IResponseConstants.EXPENSE_DELETE_SUCESS;
	}

	@Override
	public Map<String, Object> getExpenseStats() {
		Map<String, Object> response = new HashMap<>();
		response.put("totalExpenses", expenseRepository.findTotalExpenseAmount());
		response.put("pendingExpenses", expenseRepository.findTotalPendingAmount());
		response.put("approvedExpenses", expenseRepository.findTotalApprovedAmount());
		return response;
	}
}
