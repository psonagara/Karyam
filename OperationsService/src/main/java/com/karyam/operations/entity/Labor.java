package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.LaborType;
import com.karyam.operations.enu.SkillLevel;

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
@Data
@Table(name = "labor", indexes = {
	    @Index(name = "idx_labor_type", columnList = "labor_type"),
	    @Index(name = "idx_status", columnList = "status"),
	    @Index(name = "idx_assigned_project", columnList = "assigned_project_id"),
	    @Index(name = "idx_created_by", columnList = "created_by")
	})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Labor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, insertable = false, updatable = false)
	@Generated(event = EventType.INSERT)
	private String laborId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String phone;
	
	@Column(columnDefinition = "TEXT")
	private String address;
	
	@Enumerated(EnumType.STRING)
	private LaborType laborType;
	
	@Column(precision = 10, scale = 2)
	@Builder.Default
	private BigDecimal dailyWage = BigDecimal.ZERO;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_labor_assigned_project"))
	private Project assignedProject;
	
	private LocalDate joiningDate;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ActivationStatus status = ActivationStatus.ACTIVE;
	
	@Column(precision = 4, scale = 1)
	private BigDecimal experience;
	
	@Enumerated(EnumType.STRING)
	private SkillLevel skillLevel;
	
	@Column(columnDefinition = "TEXT")
	private String skills;
	
	private String emergencyContactName;
	
	private String emergencyContactPhone;
	
	private String emergencyContactRelation;
	
	private String bankName;
	
	private String accountNumber;
	
	private String ifscCode;
	
	private String accountHolder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_labor_created_by"))
	private User createdBy;
	
	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedAt;
}
