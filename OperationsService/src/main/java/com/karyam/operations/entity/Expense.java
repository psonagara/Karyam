package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ExpenseStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "expenses", indexes = {
	    @Index(name = "idx_project", columnList = "project_id"),
	    @Index(name = "idx_vendor", columnList = "vendor_id"),
	    @Index(name = "idx_category", columnList = "category"),
	    @Index(name = "idx_status", columnList = "status"),
	    @Index(name = "idx_date", columnList = "date"),
	    @Index(name = "idx_created_by", columnList = "created_by"),
	    @Index(name = "idx_approved_by", columnList = "approved_by")
	})
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, insertable = false, updatable = false)
	@Generated(event = EventType.INSERT)
	private String expenseId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expenses_project"))
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", foreignKey = @ForeignKey(name = "fk_expenses_vendor"))
	private Vendor vendor;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExpenseCategory category;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(name = "bill_number", length = 100)
	private String billNumber;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExpenseStatus status = ExpenseStatus.PENDING;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_expenses_created_by"))
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_expenses_approved_by"))
	private User approvedBy;

	@Column(name = "approved_at")
	private LocalDateTime approvedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_by", foreignKey = @ForeignKey(name = "fk_expenses_rejected_by"))
	private User rejectedBy;

	@Column(name = "rejected_at")
	private LocalDateTime rejectedAt;

	@Column(name = "rejection_reason", columnDefinition = "TEXT")
	private String rejectionReason;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false)
	private LocalDateTime updatedAt;
}
