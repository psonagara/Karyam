package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vendor_payments", indexes = {
		@Index(name = "idx_vendor", columnList = "vendor_id"),
		@Index(name = "idx_payment_date", columnList = "payment_date"),
		@Index(name = "idx_paid_by", columnList = "paid_by")
})
public class VendorPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, insertable = false, updatable = false)
	@Generated(event = EventType.INSERT)
	private String paymentId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "vendor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vendor_payments_vendor"))
	private Vendor vendor;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "payment_date", nullable = false)
	private LocalDate paymentDate;

	@Column(name = "payment_method", length = 50)
	private String paymentMethod;

	@Column(name = "reference_number", length = 100)
	private String referenceNumber;

	@Column(columnDefinition = "TEXT")
	private String remarks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paid_by", foreignKey = @ForeignKey(name = "fk_vendor_payments_paid_by"))
	private User paidBy;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
}
