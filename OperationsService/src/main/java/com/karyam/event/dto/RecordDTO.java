package com.karyam.event.dto;

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
public class RecordDTO {

	private Long id;
	private String laborId;
	private String projectId;
	private LocalDate date;
	private AttendanceStatus status;
	private BigDecimal workingHours;
	private BigDecimal overtimeHours;
	private String remarks;
}
