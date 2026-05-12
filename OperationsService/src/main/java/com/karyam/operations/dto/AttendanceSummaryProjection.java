package com.karyam.operations.dto;

import java.time.LocalDate;

public interface AttendanceSummaryProjection {
	
    LocalDate getDate();
    Long getTotal();
    Long getPresent();
    Long getAbsent();
    Long getHalfDay();
    Long getOvertime();
}