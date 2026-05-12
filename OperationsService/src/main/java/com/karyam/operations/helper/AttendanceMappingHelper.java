package com.karyam.operations.helper;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.RecordDTO;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.AttendanceRecord;
import com.karyam.operations.entity.Attendance;
import com.karyam.operations.util.CommonUtil;

public interface AttendanceMappingHelper {

	public static Attendance toAttendance(AttendanceRecord record) {
		return Attendance.builder()
				.date(record.getDate())
				.status(record.getStatus())
				.workingHours(record.getWorkingHours())
				.overtimeHours(record.getOvertimeHours())
				.remarks(record.getRemarks())
				.build();
	}
	
	public static RecordDTO toRecordDTO(Attendance attendance) {
		return RecordDTO.builder()
				.id(attendance.getId())
				.laborId(attendance.getLabor().getLaborId())
				.projectId(attendance.getProject().getProjectId())
				.date(attendance.getDate())
				.status(attendance.getStatus())
				.workingHours(attendance.getWorkingHours())
				.overtimeHours(attendance.getOvertimeHours())
				.remarks(attendance.getRemarks())
				.build();
	}
	
	public static KafkaEvent<?> createAttendaceEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Attendance", oldValue, newValue);
	}
}
