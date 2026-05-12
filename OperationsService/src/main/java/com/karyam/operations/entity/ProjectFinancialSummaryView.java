package com.karyam.operations.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_project_financial_summary")
@Getter
public class ProjectFinancialSummaryView {

	@Id
	private Long id;

	@Column(name = "project_id")
	private String projectId;

	private String name;

	@Column(precision = 15, scale = 2)
	private BigDecimal budget;

	@Column(name = "total_expenses", precision = 15, scale = 2)
	private BigDecimal totalExpenses;

	@Column(name = "remaining_budget", precision = 15, scale = 2)
	private BigDecimal remainingBudget;

	@Column(name = "pending_expenses", precision = 15, scale = 2)
	private BigDecimal pendingExpenses;

	@Column(name = "pending_expense_count")
	private Long pendingExpenseCount;
}
