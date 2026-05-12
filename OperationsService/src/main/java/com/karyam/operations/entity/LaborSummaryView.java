package com.karyam.operations.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "vw_labor_summary")
public class LaborSummaryView {

	@Id
    @Column(length = 50)
    private Long id;

    @Column(name = "labor_id", length = 50)
    private String laborId;

    private String name;
    
    private String phone;

    @Column(name = "labor_type")
    private String laborType;

    @Column(name = "daily_wage", precision = 10, scale = 2)
    private BigDecimal dailyWage;

    private String status;

    @Column(name = "assigned_project")
    private String assignedProject;

    @Column(name = "assigned_project_id_display")
    private String assignedProjectIdDisplay;

    @Column(name = "total_attendance_days")
    private Long totalAttendanceDays;

    @Column(name = "present_days")
    private Long presentDays;

    @Column(name = "total_overtime_hours", precision = 10, scale = 1)
    private BigDecimal totalOvertimeHours;
}
