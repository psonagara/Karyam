package com.karyam.operations.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.dto.response.ExpenseListResponse;
import com.karyam.operations.dto.response.ExpenseResponse;
import com.karyam.operations.enu.ExpenseStatus;
import com.karyam.operations.service.IApprovalService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.APPROVAL_API)
@Slf4j
public class ApprovalRestController {
	
	@Autowired
	private IApprovalService approvalService;

	@GetMapping("pending")
	public ResponseEntity<?> getAllPendingApprovals(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId,
			@PageableDefault(page = 0, size = 10) Pageable pageable) { 
		log.debug("Enter in ApprovalRestController.getAllPendingApprovals");
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.STATUS, ExpenseStatus.PENDING);
		
		ExpenseListResponse response = approvalService.getAllPendingApprovals(requestMap, pageable);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getApprovalById(@PathVariable(ICommonConstants.ID) Long approvalId) {
		log.debug("Enter in ApprovalRestController.getApprovalById");
		ExpenseResponse response = approvalService.getApprovalById(approvalId);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("stats")
	public ResponseEntity<?> getApprovalStats() {
		return ResponseEntity.ok(approvalService.getApprovalStats());
	}
	
	@PostMapping("{id}/approve")
	public ResponseEntity<?> approveExpense(@PathVariable(ICommonConstants.ID) Long approvalId, HttpServletRequest servletRequest) {
		log.debug("Enter in ApprovalRestController.approveExpense");
		String message = approvalService.approveExpense(approvalId, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.OK);
	}

	@PostMapping("{id}/reject")
	public ResponseEntity<?> rejectExpense(@PathVariable(ICommonConstants.ID) Long approvalId, @RequestBody Map<String, Object> requestMap, HttpServletRequest servletRequest) {
		log.debug("Enter in ApprovalRestController.rejectExpense");
		String message = approvalService.rejectExpense(approvalId, requestMap, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.OK);
	}
	
}
