package com.karyam.operations.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ProjectStatus;
import com.karyam.operations.enu.VendorCategory;
import com.karyam.operations.service.IReportService;

@RestController
@RequestMapping(IMappingConstants.REPORT_API)
public class ReportRestController {
	
	@Autowired
	private IReportService reportService;

	
	@GetMapping("expenses")
	public ResponseEntity<?> getExpenseReport(
			@RequestParam(name = ICommonConstants.DAYS, required = false) Integer days,
			@RequestParam(name = ICommonConstants.CATEGORY, required = false) ExpenseCategory category,
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId) { 
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.CATEGORY, category);
		requestMap.put(ICommonConstants.DAYS, days);
		
		return ResponseEntity.ok(reportService.getExpenseReport(requestMap));
	}

	@GetMapping("payroll")
	public ResponseEntity<?> getPayrollReport(
			@RequestParam(name = ICommonConstants.MONTH, required = false) String month,
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId) { 
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.MONTH, month);
		
		return ResponseEntity.ok(reportService.getPayrollReport(requestMap));
	}

	@GetMapping("vendors")
	public ResponseEntity<?> getVendorReport(
			@RequestParam(name = ICommonConstants.CATEGORY, required = false) VendorCategory category) { 
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.CATEGORY, category);
		
		return ResponseEntity.ok(reportService.getVendorReport(requestMap));
	}

	@GetMapping("projects")
	public ResponseEntity<?> getProjectReport(
			@RequestParam(name = ICommonConstants.STATUS, required = false) ProjectStatus status) { 
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.STATUS, status);
		
		return ResponseEntity.ok(reportService.getProjectReport(requestMap));
	}
}
