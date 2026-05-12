package com.karyam.operations.service;

import java.util.Map;

public interface IReportService {

	Map<String, Object> getExpenseReport(Map<String, Object> requestMap);
	Map<String, Object> getPayrollReport(Map<String, Object> requestMap);
	Map<String, Object> getVendorReport(Map<String, Object> requestMap);
	Map<String, Object> getProjectReport(Map<String, Object> requestMap);
}
