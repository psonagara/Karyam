package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

import com.karyam.operations.enu.PayrollStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "payroll", 
		indexes = {
				@Index(name = "idx_project", columnList = "project_id"),
				@Index(name = "idx_month", columnList = "month"),
				@Index(name = "idx_status", columnList = "status"),
				@Index(name = "idx_generated_by", columnList = "generated_by")
		},
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_labor_project_month", 
						columnNames = {"labor_id", "project_id", "month"}
						)
		}
		)
public class Payroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, insertable = false, updatable = false)
	@Generated(event = EventType.INSERT)
	private String payrollId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
			name = "labor_id", 
			nullable = false, 
			foreignKey = @ForeignKey(name = "fk_payroll_labor")
			)
	private Labor labor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
			name = "project_id", 
			nullable = false, 
			foreignKey = @ForeignKey(name = "fk_payroll_project")
			)
	private Project project;

	@Column(nullable = false, length = 7)
	private String month;

	@Column(name = "labor_type", nullable = false, length = 50)
	private String laborType;

	@Column(name = "daily_wage", nullable = false, precision = 10, scale = 2)
	private BigDecimal dailyWage;

	@Builder.Default
	@Column(name = "present_days", nullable = false)
	private Integer presentDays = 0;

	@Builder.Default
	@Column(name = "overtime_hours", nullable = false, precision = 6, scale = 1)
	private BigDecimal overtimeHours = BigDecimal.ZERO;

	@Builder.Default
	@Column(name = "basic_salary", nullable = false, precision = 12, scale = 2)
	private BigDecimal basicSalary = BigDecimal.ZERO;

	@Builder.Default
	@Column(name = "overtime_pay", nullable = false, precision = 12, scale = 2)
	private BigDecimal overtimePay = BigDecimal.ZERO;

	@Builder.Default
	@Column(name = "total_salary", nullable = false, precision = 12, scale = 2)
	private BigDecimal totalSalary = BigDecimal.ZERO;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PayrollStatus status = PayrollStatus.PENDING;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;
	
	private String paymentMethod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "paid_by", 
			foreignKey = @ForeignKey(name = "fk_payroll_paid_by")
			)
	private User paidBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "generated_by", 
			foreignKey = @ForeignKey(name = "fk_payroll_generated_by")
			)
	private User generatedBy;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false)
	private LocalDateTime updatedAt;
}
