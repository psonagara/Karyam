package com.karyam.operations.dto;

import java.math.BigDecimal;

public interface WorkerAttendanceProjection {
	
    Long getId();
    String getLaborId();
    String getName();
    String getLaborType();
    BigDecimal getDaily_wage();
    String getAssignedProject();
    String getAssignedProjectId();
    String getAttendance();
    BigDecimal getWorkingHours();
    BigDecimal getOvertimeHours();
    String getRemarks();
}