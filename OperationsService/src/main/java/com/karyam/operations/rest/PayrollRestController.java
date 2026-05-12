package com.karyam.operations.rest;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.request.PayrollRequest;
import com.karyam.operations.dto.response.PayrollListResponse;
import com.karyam.operations.dto.response.PayrollResponse;
import com.karyam.operations.enu.PayrollStatus;
import com.karyam.operations.service.IPayrollService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.PAYROLL_API)
@Slf4j
public class PayrollRestController {
	
	@Autowired
	private IPayrollService payrollService;
	
	@PostMapping("generate/{projectId}")
	public ResponseEntity<?> generatePayroll(@PathVariable(ICommonConstants.PROJECT_ID) Long projectId,
			@RequestParam(ICommonConstants.MONTH) Integer month, @RequestParam(ICommonConstants.YEAR) Integer year, 
			HttpServletRequest servletRequest) {

		Map<String, Object> requestMap = new HashedMap<>();
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.MONTH, month);
		requestMap.put(ICommonConstants.YEAR, year);
		
		log.debug("Enter in PayrollRestController.generatePayroll, " + requestMap);
		
		Map<String, Object> response = payrollService.generatePayroll(requestMap, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponse(IResponseConstants.PAYROLL_GENERATE_SUCESS, HttpStatus.OK, response);
	}
	
	@GetMapping
	public ResponseEntity<?> getPayrolls(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId,
			@RequestParam(name = ICommonConstants.MONTH, required = false) Integer month,
			@RequestParam(name = ICommonConstants.YEAR, required = false) Integer year,
			@RequestParam(name = ICommonConstants.STATUS, required = false) PayrollStatus status,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Map<String, Object> requestMap = new HashedMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.MONTH, month);
		requestMap.put(ICommonConstants.YEAR, year);
		requestMap.put(ICommonConstants.STATUS, status);
		
		log.debug("Enter in PayrollRestController.getPayrolls, " + requestMap);
		PayrollListResponse response = payrollService.filterPayrolls(requestMap, pageable);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getPayroll(@PathVariable(name = ICommonConstants.ID) Long payrollId) {
		log.debug("Enter in PayrollRestController.getPayroll, " + payrollId);
		PayrollResponse response = payrollService.getPayrollById(payrollId);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@PostMapping("{id}/mark-paid")
	public ResponseEntity<?> markPayrollPaid(@PathVariable(name = ICommonConstants.ID) Long payrollId,
			@RequestBody Map<String, Object> requestMap,
			HttpServletRequest servletRequest) {
		log.debug("Enter in PayrollRestController.markPayrollPaid, " + payrollId);
		String message = payrollService.markPayrollPaid(payrollId, requestMap, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.OK);
	}
	
	@PostMapping("mark-all-paid")
	public ResponseEntity<?> markAllPayrollPaid(@RequestBody PayrollRequest request, HttpServletRequest servletRequest) {
		log.debug("Enter in PayrollRestController.markAllPayrollPaid, " + request);
		Map<String, Object> response = payrollService.markAllPayrollPaid(request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("stats")
	public ResponseEntity<?> getPayrollStats() {
		return ResponseEntity.ok(payrollService.getPayrollStats());
	}
	
	@GetMapping("export")
	public void exportPayroll(
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId,
			@RequestParam(name = ICommonConstants.MONTH, required = false) Integer month,
			@RequestParam(name = ICommonConstants.YEAR, required = false) Integer year,
			HttpServletResponse response) throws IOException {
		
		Map<String, Object> requestMap = new HashedMap<>();
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.MONTH, month);
		requestMap.put(ICommonConstants.YEAR, year);
		
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"payroll-export.csv\"");
		payrollService.exportPayrollToCsv(response.getWriter(), requestMap);
	}
}