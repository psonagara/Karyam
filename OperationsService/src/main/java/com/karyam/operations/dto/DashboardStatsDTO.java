package com.karyam.operations.dto;

import java.math.BigDecimal;

public interface DashboardStatsDTO {

	Long getTotalProjects();
    Long getActiveLabors();
    Long getPendingApprovals();
    BigDecimal getMonthlyExpenses();
}
