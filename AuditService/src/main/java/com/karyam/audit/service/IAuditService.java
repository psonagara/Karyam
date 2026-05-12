package com.karyam.audit.service;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.audit.dto.response.AuditListResponse;
import com.karyam.audit.dto.response.AuditResponse;
import com.karyam.audit.entity.AuditLog;
import com.karyam.audit.entity.User;

public interface IAuditService {

	void saveAuditLog(AuditLog auditLog);
	void saveAllAuditLogs(List<AuditLog> auditLogs);
	User getUserById(Long userId);
	AuditListResponse filterAuditLogs(Map<String, Object> requestMap, Pageable pageable);
	Map<String, Object> getAuditStats();
	void exportAuditLogsToCsv(PrintWriter writer, Map<String, Object> requestMap);
	void exportAuditLogsToJson(OutputStream outputStream, Map<String, Object> requestMap);
	void exportAuditLogsToPdf(OutputStream outputStream, Map<String, Object> requestMap);
	AuditResponse getAuditLogById(Long logId);
}
