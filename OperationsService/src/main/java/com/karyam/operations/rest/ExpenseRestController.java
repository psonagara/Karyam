package com.karyam.operations.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.dto.request.ExpenseRequest;
import com.karyam.operations.dto.response.ExpenseListResponse;
import com.karyam.operations.dto.response.ExpenseResponse;
import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ExpenseStatus;
import com.karyam.operations.service.IExpenseService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.EXPENSE_API)
@Slf4j
public class ExpenseRestController {

	@Autowired
	private IExpenseService expenseService;

	@PostMapping
	public ResponseEntity<?> registerExpense(@RequestBody ExpenseRequest request, HttpServletRequest servletRequest) {
		log.debug("Enter in ExpenseRestController.registerExpense, " + request);
		String message = expenseService.createExpense(request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> getAllExpense(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId,
			@RequestParam(name = ICommonConstants.CATEGORY, required = false) ExpenseCategory category,
			@RequestParam(name = ICommonConstants.STATUS, required = false) ExpenseStatus status,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.CATEGORY, category);
		requestMap.put(ICommonConstants.STATUS, status);
		
		log.debug("Enter in ExpenseRestController.getAllExpense, " + requestMap);
		ExpenseListResponse response = expenseService.filterExpense(requestMap, pageable);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("stats")
	public ResponseEntity<?> getExpenseStats() {
		return ResponseEntity.ok(expenseService.getExpenseStats());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getExpense(@PathVariable(name = ICommonConstants.ID) Long expenseId) {
		log.debug("Enter in ExpenseRestController.getExpense, " + expenseId);
		ExpenseResponse response = expenseService.getExpenseById(expenseId);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateExpense(
			@PathVariable(name = ICommonConstants.ID) Long expenseId,
			@RequestBody ExpenseRequest request,
			HttpServletRequest servletRequest) {
		
		log.debug("Enter in ExpenseRestController.updateExpense, id:" + expenseId + ", request:" + request);
		String message = expenseService.updateExpenseDetail(expenseId, request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable(name = ICommonConstants.ID) Long expenseId, HttpServletRequest servletRequest) {
		log.debug("Enter in ExpenseRestController.deleteExpense, " + expenseId);
		String message = expenseService.deleteExpenseById(expenseId, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseContent(message, HttpStatus.OK);
	}
}
