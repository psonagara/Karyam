package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.VendorCategory;

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

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendors", indexes = {
		@Index(name = "idx_category", columnList = "category"),
		@Index(name = "idx_status", columnList = "status"),
		@Index(name = "idx_created_by", columnList = "created_by")
})
@Data
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(name = "contact_person", length = 100)
	private String contactPerson;

	@Column(nullable = false, unique = true, length = 10)
	private String phone;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VendorCategory category;

	@Column(name = "payment_terms", length = 100)
	private String paymentTerms;

	@Column(columnDefinition = "TEXT")
	private String address;

	@Builder.Default
	@Column(name = "due_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal dueAmount = BigDecimal.ZERO;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ActivationStatus status = ActivationStatus.ACTIVE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_vendors_created_by"))
	private User createdBy;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedAt;
}
