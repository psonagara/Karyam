package com.karyam.operations.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDashboardStats {

	private BigDecimal totalAmount;
	private BigDecimal pendingAmount;
	private BigDecimal paidThisMonthAmount;
	private Long totalDistinctWorkers;
}
