package com.karyam.operations.dto;

import java.math.BigDecimal;

public interface PayrollProjection {
    
	Long getId();
    String getPayrollId();
    String getLaborId();
    String getLaborName();
    String getLaborType();
    BigDecimal getDailyWage();
    Integer getPresentDays();
    BigDecimal getOvertimeHours();
    BigDecimal getBasicSalary();
    BigDecimal getOvertimePay();
    BigDecimal getTotalSalary();
    String getStatus();
}