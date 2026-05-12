package com.karyam.operations.dto;

import java.math.BigDecimal;

public interface BudgetAlertDTO {
    Long getProjectId();
    String getProjectName();
    BigDecimal getBudget();
    BigDecimal getUsed();
    Double getPercentage();
    String getLevel();
}