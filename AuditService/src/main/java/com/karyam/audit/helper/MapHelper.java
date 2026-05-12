package com.karyam.audit.helper;

import java.util.HashMap;
import java.util.Map;

import com.karyam.event.dto.ApprovalDTO;
import com.karyam.event.dto.ExpenseDTO;
import com.karyam.event.dto.LaborDTO;
import com.karyam.event.dto.PayrollDTO;
import com.karyam.event.dto.ProjectDTO;
import com.karyam.event.dto.RecordDTO;
import com.karyam.event.dto.VendorDTO;
import com.karyam.event.dto.VendorPaymetDTO;

public interface MapHelper {

	public static Map<String, Object> createValueMap(ProjectDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("projectId", dto.getProjectId());
        valueMap.put("projectDisplayId", dto.getProjectDisplayId());
        valueMap.put("name", dto.getProjectName());
        valueMap.put("location", dto.getLocation() != null ? dto.getLocation() : "");
        valueMap.put("budget", dto.getBudget());
        valueMap.put("currency", dto.getCurrency());
        valueMap.put("startDate", dto.getStartDate());
        valueMap.put("endDate", dto.getEndDate());
        valueMap.put("status", dto.getStatus());
        valueMap.put("description", dto.getDescription());
        valueMap.put("manager", dto.getManager());
        valueMap.put("contactPerson", dto.getContactPerson());
        valueMap.put("contactPhone", dto.getContactPhone());
        valueMap.put("contactEmail", dto.getContactEmail());
        return valueMap;
	}

	public static Map<String, Object> createValueMap(LaborDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("laborId", dto.getLaborId());
		valueMap.put("laborDisplayId", dto.getLaborDisplayId());
		valueMap.put("name", dto.getName());
		valueMap.put("phone", dto.getPhone());
		valueMap.put("address", dto.getAddress());
		valueMap.put("laborType", dto.getLaborType());
		valueMap.put("dailyWage", dto.getDailyWage());
		valueMap.put("assignedProjectId", dto.getAssignedProjectId());
		valueMap.put("joiningDate", dto.getJoiningDate());
		valueMap.put("status", dto.getStatus());
		valueMap.put("experience", dto.getExperience());
		valueMap.put("skillLevel", dto.getSkillLevel());
		valueMap.put("skills", dto.getSkills());
		valueMap.put("emergencyContactName", dto.getEmergencyContactName());
		valueMap.put("emergencyContactPhone", dto.getEmergencyContactPhone());
		valueMap.put("emergencyContactRelation", dto.getEmergencyContactRelation());
		valueMap.put("bankName", dto.getBankName());
		valueMap.put("accountNumber", dto.getAccountNumber());
		valueMap.put("ifscCode", dto.getIfscCode());
		valueMap.put("accountHolder", dto.getAccountHolder());
		return valueMap;
	}

	public static Map<String, Object> createValueMap(RecordDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("attendanceId", dto.getId());
		valueMap.put("laborId", dto.getLaborId());
		valueMap.put("projectId", dto.getProjectId());
		valueMap.put("date", dto.getDate());
		valueMap.put("status", dto.getStatus());
		valueMap.put("workingHours", dto.getWorkingHours());
		valueMap.put("overtimeHours", dto.getOvertimeHours());
		valueMap.put("remarks", dto.getRemarks());
		return valueMap;
	}

	public static Map<String, Object> createValueMap(VendorDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("vendorId", dto.getId());
		valueMap.put("name", dto.getName());
		valueMap.put("contactPerson", dto.getContactPerson());
		valueMap.put("phone", dto.getPhone());
		valueMap.put("email", dto.getEmail());
		valueMap.put("category", dto.getCategory());
		valueMap.put("paymentTerms", dto.getPaymentTerms());
		valueMap.put("address", dto.getAddress());
		valueMap.put("dueAmount", dto.getDueAmount());
		valueMap.put("status", dto.getStatus());
		return valueMap;
	}
	
	public static Map<String, Object> createValueMap(VendorPaymetDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("id", dto.getId());
		valueMap.put("paymentId", dto.getPaymentId());
		valueMap.put("vendorId", dto.getVendorId());
		valueMap.put("amount", dto.getAmount());
		valueMap.put("paymentDate", dto.getPaymentDate());
		valueMap.put("paymentMethod", dto.getPaymentMethod());
		valueMap.put("referenceNumber", dto.getReferenceNumber());
		valueMap.put("remarks", dto.getRemarks());
		return valueMap;
	}

	public static Map<String, Object> createValueMap(ExpenseDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("id", dto.getId());
		valueMap.put("expenseId", dto.getExpenseId());
		valueMap.put("projectId", dto.getProjectId());
		valueMap.put("vendorId", dto.getVendorId());
		valueMap.put("category", dto.getCategory());
		valueMap.put("amount", dto.getAmount());
		valueMap.put("date", dto.getDate());
		valueMap.put("description", dto.getDescription());
		valueMap.put("billNumber", dto.getBillNumber());
		valueMap.put("status", dto.getStatus());
		valueMap.put("approvedBy", dto.getApprovedBy());
		valueMap.put("approvedAt", dto.getApprovedAt());
		valueMap.put("rejectedBy", dto.getRejectedBy());
		valueMap.put("rejectedAt", dto.getRejectedAt());
		valueMap.put("rejectionReason", dto.getRejectionReason());
		return valueMap;
	}
	
	public static Map<String, Object> createValueMap(ApprovalDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("id", dto.getId());
		valueMap.put("rejectionReason", dto.getRejectionReason());
		return valueMap;
	}

	public static Map<String, Object> createValueMap(PayrollDTO dto) {
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("id", dto.getId());
		valueMap.put("payrollId", dto.getPayrollId());
		valueMap.put("laborId", dto.getLaborId());
		valueMap.put("laborName", dto.getLaborName());
		valueMap.put("laborType", dto.getLaborType());
		valueMap.put("dailyWage", dto.getDailyWage());
		valueMap.put("presentDays", dto.getPresentDays());
		valueMap.put("overtimeHours", dto.getOvertimeHours());
		valueMap.put("basicSalary", dto.getBasicSalary());
		valueMap.put("overtimePay", dto.getOvertimePay());
		valueMap.put("totalSalary", dto.getTotalSalary());
		valueMap.put("status", dto.getStatus());
		return valueMap;
	}
}
