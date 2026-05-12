package com.karyam.operations.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.dto.AttendanceSummaryProjection;
import com.karyam.operations.dto.WorkerAttendanceProjection;
import com.karyam.operations.dto.request.AttendanceRequest;
import com.karyam.operations.enu.LaborType;
import com.karyam.operations.service.IAttendanceService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.ATTENDANCE_API)
@Slf4j
public class AttendanceRestController {
	
	@Autowired
	private IAttendanceService attendanceService;
	
	@PostMapping("mark")
	public ResponseEntity<?> markAttendace(@RequestBody AttendanceRequest request, HttpServletRequest servletRequest) {
		log.debug("Enter in AttendanceRestController.markAttendace, " + request);
		String message = attendanceService.markAttendance(request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.OK);
	}
	
	@GetMapping("workers")
	public ResponseEntity<?> getWorkersForAttendance(@RequestParam(name = "projectId", required = false) Long projectId,
			@RequestParam("date") LocalDate date, @RequestParam(name = "laborType", required = false) LaborType laborType) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.DATE, date);
		requestMap.put(ICommonConstants.LABOR_TYPE, laborType);
		
		log.debug("Enter in AttendanceRestController.getWorkersForAttendance, " + requestMap);
		List<WorkerAttendanceProjection> workersForAttendance = attendanceService.getWorkersForAttendance(requestMap);
		return CommonUtil.prepareResponseContent(workersForAttendance, HttpStatus.OK);
	}
	
	@GetMapping("today/summary")
	public ResponseEntity<?> getAttendaceSummary() {
		
		log.debug("Enter in AttendanceRestController.getAttendaceSummary");
		AttendanceSummaryProjection attendanceSummary = attendanceService.getAttendanceSummary();
		return CommonUtil.prepareResponseContent(attendanceSummary, HttpStatus.OK);
	}
	
	@GetMapping("export")
	public void exportAttendace(
			@RequestParam(name = "startDate", required = false) LocalDate startDate,
		    @RequestParam(name = "endDate", required = false) LocalDate endDate,
		    @RequestParam(name = "projectId", required = false) Long projectId,
		    HttpServletResponse response) throws IOException {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		requestMap.put(ICommonConstants.START_DATE, startDate);
		requestMap.put(ICommonConstants.END_DATE, endDate);
		
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attendance-export.csv\"");
		attendanceService.exportAttendanceToCsv(response.getWriter(), requestMap);
	}
}
