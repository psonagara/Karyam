package com.karyam.operations.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class AttendanceRequest {

	private LocalDate date;
	private List<AttendanceRecord> records;
}
