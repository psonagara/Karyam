package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.karyam.operations.enu.AttendanceStatus;

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
@Entity
@Table(
	    name = "attendance", 
	    indexes = {
	        @Index(name = "idx_project", columnList = "project_id"),
	        @Index(name = "idx_date", columnList = "date"),
	        @Index(name = "idx_status", columnList = "status"),
	        @Index(name = "idx_marked_by", columnList = "marked_by")
	    },
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_labor_date", 
	            columnNames = {"labor_id", "date"}
	        )
	    }
	)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "labor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_attendance_labor"))
	private Labor labor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_attendance_project"))
	private Project project;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AttendanceStatus status;
	
	@Column(nullable = false, precision = 4, scale = 1)
	@Builder.Default
	private BigDecimal workingHours = BigDecimal.ZERO;
	
	@Column(nullable = false, precision = 4, scale = 1)
	@Builder.Default
	private BigDecimal overtimeHours = BigDecimal.ZERO;
	
	@Column(columnDefinition = "TEXT")
	private String remarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marked_by", foreignKey = @ForeignKey(name = "fk_attendance_marked_by"))
	private User markedBy;
	
	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedAt;
}
