package com.karyam.operations.dto.response;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.PayrollDTO;
import com.karyam.operations.dto.PayrollProjection;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.entity.Payroll;
import com.karyam.operations.util.CommonUtil;

public interface PayrollMappingHelper {

	public static PayrollResponse toPayrollResponse(Payroll payroll) {
		return PayrollResponse.builder()
				.id(payroll.getId())
				.payrollId(payroll.getPayrollId())
				.laborId(payroll.getLabor().getLaborId())
				.laborName(payroll.getLabor().getName())
				.projectName(payroll.getProject().getName())
				.month(Integer.parseInt(payroll.getMonth().substring(4)))
				.year(Integer.parseInt(payroll.getMonth().substring(0, 4)))
				.daysPresent(payroll.getPresentDays())
				.basicSalary(payroll.getBasicSalary())
				.overtimeAmount(payroll.getOvertimePay())
				.totalAmount(payroll.getTotalSalary())
				.status(payroll.getStatus())
				.generatedAt(payroll.getCreatedAt())
				.generatedByName(payroll.getGeneratedBy().getName())
				.dailyWage(payroll.getDailyWage())
				.overtimeHours(payroll.getOvertimeHours())
				.paidAt(payroll.getPaidAt())
				.paymentMethod(payroll.getPaymentMethod())
				.build();
	}
	
	public static KafkaEvent<?> createPayrollEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Payroll", oldValue, newValue);
	}
	
	public static PayrollDTO toPayrollDTO(PayrollProjection payroll) {
		return PayrollDTO.builder()
				.id(payroll.getId())
				.payrollId(payroll.getPayrollId())
				.laborId(payroll.getLaborId())
				.laborName(payroll.getLaborName())
				.laborType(payroll.getLaborType())
				.dailyWage(payroll.getDailyWage())
				.presentDays(payroll.getPresentDays())
				.overtimeHours(payroll.getOvertimeHours())
				.basicSalary(payroll.getBasicSalary())
				.overtimePay(payroll.getOvertimePay())
				.totalSalary(payroll.getTotalSalary())
				.status(payroll.getStatus())
				.build();
	}
}
