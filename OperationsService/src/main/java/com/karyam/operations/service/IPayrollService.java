package com.karyam.operations.service;

import java.io.PrintWriter;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.operations.dto.PayrollDashboardStats;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.PayrollRequest;
import com.karyam.operations.dto.response.PayrollListResponse;
import com.karyam.operations.dto.response.PayrollResponse;

public interface IPayrollService {

	Map<String, Object> generatePayroll(Map<String, Object> requestMap, RequestMetadata data);
	PayrollListResponse filterPayrolls(Map<String, Object> requestMap, Pageable pageable);
	PayrollResponse getPayrollById(Long payrollId);
	String markPayrollPaid(Long payrollId, Map<String, Object> requestMap, RequestMetadata data);
	Map<String, Object> markAllPayrollPaid(PayrollRequest request, RequestMetadata data);
	PayrollDashboardStats getPayrollStats();
	void exportPayrollToCsv(PrintWriter writer, Map<String, Object> requestMap);
}
