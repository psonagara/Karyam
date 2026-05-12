package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.karyam.operations.enu.PayrollStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_payroll_summary")
@Getter
public class PayrollSummaryView {

	@Id
	private Long id;

	@Column(name = "payroll_id")
	private String payrollId;

	@Column(name = "project_id")
	private Long projectId;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "labor_id")
	private Long laborId;

	@Column(name = "labor_display_id")
	private String laborDisplayId;

	@Column(name = "labor_name")
	private String laborName;

	private String month;

	@Column(name = "labor_type")
	private String laborType;

	@Column(name = "daily_wage", precision = 10, scale = 2)
	private BigDecimal dailyWage;

	@Column(name = "present_days")
	private Integer presentDays;

	@Column(name = "overtime_hours", precision = 6, scale = 1)
	private BigDecimal overtimeHours;

	@Column(name = "basic_salary", precision = 12, scale = 2)
	private BigDecimal basicSalary;

	@Column(name = "overtime_pay", precision = 12, scale = 2)
	private BigDecimal overtimePay;

	@Column(name = "total_salary", precision = 12, scale = 2)
	private BigDecimal totalSalary;

	@Enumerated(EnumType.STRING)
	private PayrollStatus status;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;

	@Column(name = "paid_by_name")
	private String paidByName;
}
