package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ExpenseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_expense_summary")
@Getter
public class ExpenseSummaryView {

	@Id
    private Long id;

    @Column(name = "expense_id")
    private String expenseId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "created_by_name")
    private String createdByName;

    @Column(name = "approved_by_name")
    private String approvedByName;

    @Column(name = "rejected_by_name")
    private String rejectedByName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "days_since_expense")
    private Integer daysSinceExpense;
}
