package com.karyam.operations.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.service.IDashboardService;

@RestController
@RequestMapping(IMappingConstants.DASHBOARD_API)
public class DashboardRestController {
	
	@Autowired
	private IDashboardService dashboardService;

	@GetMapping("stats")
	public ResponseEntity<?> getDashboardStats() {
		return ResponseEntity.ok(dashboardService.getDashboardStats());
	}
	
	@GetMapping("budget/alerts")
	public ResponseEntity<?> getBudgetAlerts() {
		return ResponseEntity.ok(dashboardService.getBudgetAlerts());
	}
}
