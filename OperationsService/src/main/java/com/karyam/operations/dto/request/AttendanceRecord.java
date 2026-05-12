package com.karyam.operations.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.karyam.operations.enu.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecord {

	private Long laborId;
	private LocalDate date;
	private AttendanceStatus status;
	private BigDecimal workingHours;
	private BigDecimal overtimeHours;
	private String remarks;
}
