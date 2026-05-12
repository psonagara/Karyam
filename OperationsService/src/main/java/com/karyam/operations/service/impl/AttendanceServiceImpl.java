package com.karyam.operations.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.RecordDTO;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.AttendanceSummaryProjection;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.WorkerAttendanceProjection;
import com.karyam.operations.dto.request.AttendanceRecord;
import com.karyam.operations.dto.request.AttendanceRequest;
import com.karyam.operations.entity.Attendance;
import com.karyam.operations.entity.Labor;
import com.karyam.operations.entity.Project;
import com.karyam.operations.entity.User;
import com.karyam.operations.enu.LaborType;
import com.karyam.operations.exception.BadRequestException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.AttendanceMappingHelper;
import com.karyam.operations.repo.AttendanceRepository;
import com.karyam.operations.repo.LaborRepository;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.service.IAttendanceService;
import com.karyam.operations.util.JwtUtil;
import com.opencsv.CSVWriter;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceServiceImpl implements IAttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private LaborRepository laborRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;

	@Override
	public String markAttendance(AttendanceRequest request, RequestMetadata data) {
		List<Attendance> attendances = request.getRecords().stream()
				.map((record) -> prepareAttendance(record))
				.collect(Collectors.toList());
		try {
			List<Attendance> savedAttendance = attendanceRepository.saveAll(attendances);
			log.info("Attendance records saved date: {}, by: {}", request.getDate(), JwtUtil.getUserId());
			
			// prepare and publish to kafka
			List<RecordDTO> newValue = savedAttendance.stream()
					.map(AttendanceMappingHelper::toRecordDTO)
					.collect(Collectors.toList());
			KafkaEvent<?> attendaceEvent = AttendanceMappingHelper.createAttendaceEvent(data, "attendance.mark", null, newValue);
			kafkaProducer.publishAttendanceEvent(attendaceEvent);
		} catch (Exception e) {
			log.error("Failed to mark attendance for date: {}, by {}", request.getDate(), JwtUtil.getUserId());
			throw new InternalServerException(IExceptionConstants.ATTNEDANCE_FAIL);
		}
		return IResponseConstants.ATTENDANCE_SUCCESS;
	}
	@Override
	public List<WorkerAttendanceProjection> getWorkersForAttendance(Map<String, Object> requestMap) {
		return laborRepository.getWorkersForAttendance(
				(LocalDate) requestMap.get(ICommonConstants.DATE), 
				(Long) requestMap.get(ICommonConstants.PROJECT_ID), 
				(LaborType) requestMap.get(ICommonConstants.LABOR_TYPE));
	}

	@Override
	public AttendanceSummaryProjection getAttendanceSummary() {
		return attendanceRepository.getAttendanceSummary(LocalDate.now());
	}
	
	@Override
	@Transactional
	public void exportAttendanceToCsv(PrintWriter writer, Map<String, Object> requestMap) {
		try (CSVWriter csvWriter = new CSVWriter(writer)) {
			
			String[] header = {"Id", "Date", "Labor ID", "Name", "Status", "Working Hours", "Overtime Hours", "Remarks"};
			csvWriter.writeNext(header);
			
			try (Stream<Attendance> stream = 
					attendanceRepository.streamAllByFilters(
					(LocalDate) requestMap.get(ICommonConstants.START_DATE), 
					(LocalDate) requestMap.get(ICommonConstants.END_DATE), 
					(Long) requestMap.get(ICommonConstants.PROJECT_ID)
					)) {
				
				stream.forEach(record -> {
					csvWriter.writeNext(new String[] {
							String.valueOf(record.getId()),
							String.valueOf(record.getDate()),
							record.getLabor().getLaborId(),
							record.getLabor().getName(),
							String.valueOf(record.getStatus()),
							String.valueOf(record.getWorkingHours()),
							String.valueOf(record.getOvertimeHours()),
							String.valueOf(record.getRemarks())
					});
				});
			}
		} catch (IOException e) {
			throw new InternalServerException(IExceptionConstants.CSV_GENERATION_FAIL);
		}
	}
	
	private Attendance prepareAttendance(AttendanceRecord record) {
		Long laborId = record.getLaborId();
		Optional<Labor> optional = laborRepository.findById(laborId);
		if (optional.isEmpty()) {
			throw new ResourceNotFoundException(IExceptionConstants.LABOR_NOT_FOUND + laborId);
		}
		Labor labor = optional.get();
		Project assignedProject = labor.getAssignedProject();
		if (assignedProject == null) {
			throw new BadRequestException(IExceptionConstants.LABOR_NOT_ASSIGNED_TO_PROJECT);
		}
		User user = userRepository.findById(JwtUtil.getUserId()).get();

		Attendance attendance = AttendanceMappingHelper.toAttendance(record);
		attendance.setLabor(labor);
		attendance.setProject(labor.getAssignedProject());
		attendance.setMarkedBy(user);
		return attendance;
	}

}
