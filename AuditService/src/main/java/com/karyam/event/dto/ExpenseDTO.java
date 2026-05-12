package com.karyam.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.karyam.audit.enu.ExpenseCategory;
import com.karyam.audit.enu.ExpenseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDTO {

	private Long id;
	private String expenseId;
	private Long projectId;
	private Long vendorId;
	private ExpenseCategory category;
	private BigDecimal amount;
	private LocalDate date;
	private String description;
	private String billNumber;
	private ExpenseStatus status;
	private Long createdBy;
	private Long approvedBy;
	private LocalDateTime approvedAt;
	private Long rejectedBy;
	private LocalDateTime rejectedAt;
	private String rejectionReason;
}
