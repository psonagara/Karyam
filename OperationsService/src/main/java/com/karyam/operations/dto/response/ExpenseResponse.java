package com.karyam.operations.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ExpenseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
	
    private Long id;
    private String expenseId;
    private Long projectId;
    private String projectName;
    private Long vendorId;
    private String vendorName;
    private ExpenseCategory category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String billNumber;
    private ExpenseStatus status;
    private Long createdBy;
    private String createdByName;
    private Long approvedBy;
    private String approvedByName;
    private Long rejectedBy;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
