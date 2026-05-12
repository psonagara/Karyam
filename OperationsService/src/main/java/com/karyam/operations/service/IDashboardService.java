package com.karyam.operations.service;

import java.util.List;

import com.karyam.operations.dto.BudgetAlertDTO;
import com.karyam.operations.dto.DashboardStatsDTO;

public interface IDashboardService {

	DashboardStatsDTO getDashboardStats();
	List<BudgetAlertDTO> getBudgetAlerts();
}
