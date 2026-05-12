package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

import com.karyam.operations.enu.ProjectStatus;

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
@Table(name = "projects", indexes = {
	    @Index(name = "idx_status", columnList = "status"),
	    @Index(name = "idx_start_date", columnList = "start_date"),
	    @Index(name = "idx_end_date", columnList = "end_date"),
	    @Index(name = "idx_created_by", columnList = "created_by")
	})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, insertable = false, updatable = false)
	@Generated(event = EventType.INSERT)
	private String projectId;
	
	@Column(nullable = false)
	private String name;
	
	private String location;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(precision = 15, scale = 2)
	@Builder.Default
	private BigDecimal budget = BigDecimal.ZERO;
	
	@Column(nullable = false)
	@Builder.Default
	private String currency = "INR";
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String manager;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ProjectStatus status = ProjectStatus.PENDING;
	
	private String contactPerson;
	
	private String contactPhone;
	
	private String contactEmail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_projects_created_by"), updatable = false)
	private User createdBy;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updateAt;
}
