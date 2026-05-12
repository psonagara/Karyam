package com.karyam.operations.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.karyam.operations.enu.PayrollStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponse {
	
	private Long id;
	private String payrollId;
	private String laborId;
	private String laborName;
	private String projectName;
	private Integer month;
	private Integer year;
	private Integer daysPresent;
	private BigDecimal basicSalary;
	private BigDecimal overtimeAmount;
	private BigDecimal totalAmount;
	private PayrollStatus status;
	private LocalDateTime generatedAt;
	private String generatedByName;
	private BigDecimal dailyWage;
	private BigDecimal overtimeHours;
	private LocalDateTime paidAt;
	private String paymentMethod;
}
