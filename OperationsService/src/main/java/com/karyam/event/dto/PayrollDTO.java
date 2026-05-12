package com.karyam.event.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDTO {
	
    private Long id;
    private String payrollId;
    private String laborId;
    private String laborName;
    private String laborType;
    private BigDecimal dailyWage;
    private Integer presentDays;
    private BigDecimal overtimeHours;
    private BigDecimal basicSalary;
    private BigDecimal overtimePay;
    private BigDecimal totalSalary;
    private String status;
}