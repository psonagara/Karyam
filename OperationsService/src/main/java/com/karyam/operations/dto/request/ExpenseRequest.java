package com.karyam.operations.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.karyam.operations.enu.ExpenseCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {
	
    private Long projectId;
    private Long vendorId;
    private ExpenseCategory category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String billNumber;
}
