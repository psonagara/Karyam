package com.karyam.operations.service;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.karyam.operations.dto.AttendanceSummaryProjection;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.WorkerAttendanceProjection;
import com.karyam.operations.dto.request.AttendanceRequest;

public interface IAttendanceService {

	String markAttendance(AttendanceRequest request, RequestMetadata data);
	List<WorkerAttendanceProjection> getWorkersForAttendance(Map<String, Object> requestMap);
	AttendanceSummaryProjection getAttendanceSummary();
	void exportAttendanceToCsv(PrintWriter writer, Map<String, Object> requestMap);
}
