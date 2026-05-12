package com.karyam.operations.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karyam.operations.dto.BudgetAlertDTO;
import com.karyam.operations.dto.DashboardStatsDTO;
import com.karyam.operations.repo.ProjectRepository;
import com.karyam.operations.service.IDashboardService;

@Service
public class DashboardServiceImpl implements IDashboardService {
	
	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public DashboardStatsDTO getDashboardStats() {
		return projectRepository.findDashboardStats();
	}

	@Override
	public List<BudgetAlertDTO> getBudgetAlerts() {
		return projectRepository.getBudgetAlerts();
	}

}
