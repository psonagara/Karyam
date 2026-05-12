package com.karyam.audit.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.audit.constant.ICommonConstants;
import com.karyam.audit.constant.IMappingConstants;
import com.karyam.audit.dto.response.AuditListResponse;
import com.karyam.audit.service.IAuditService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;

@RestController
@RequestMapping(IMappingConstants.AUDIT_API)
public class AuditRestController {
	
	@Autowired
	private IAuditService auditService;

	@GetMapping
	public ResponseEntity<?> getAuditLogs(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.ACTION, required = false) String action,
			@RequestParam(name = ICommonConstants.ENTITY, required = false) String entity,
			@RequestParam(name = ICommonConstants.USER_ID, required = false) Long userId,
			@RequestParam(name = ICommonConstants.DAYS) Long days,
			@PageableDefault(page = 0, size = 20) Pageable pageable) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.ACTION, action);
		requestMap.put(ICommonConstants.ENTITY, entity);
		requestMap.put(ICommonConstants.USER_ID, userId);
		requestMap.put(ICommonConstants.DAYS, days);
		
		AuditListResponse auditLogs = auditService.filterAuditLogs(requestMap, pageable);
		return ResponseEntity.ok(auditLogs);
	}
	
	@GetMapping("stats")
	public ResponseEntity<?> getAuditStats() {
		return ResponseEntity.ok(auditService.getAuditStats());
	}
	
	@GetMapping("export")
	public void exportAuditLogs(
			@RequestParam(name = ICommonConstants.DAYS) Long days,
			@RequestParam(name = ICommonConstants.FORMAT) String format,
			@RequestParam(name = ICommonConstants.ACTION, required = false) String action,
			@RequestParam(name = ICommonConstants.ENTITY, required = false) String entity,
			HttpServletResponse response) throws IOException {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.ACTION, action);
		requestMap.put(ICommonConstants.ENTITY, entity);
		requestMap.put(ICommonConstants.DAYS, days);
		
		if (format.equalsIgnoreCase("CSV")) {
			response.setContentType("text/csv");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit-logs.csv\"");
			auditService.exportAuditLogsToCsv(response.getWriter(), requestMap);
		} else if (format.equalsIgnoreCase("JSON")) {
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit-logs.json\"");
	        auditService.exportAuditLogsToJson(response.getOutputStream(), requestMap);
	    } else if (format.equalsIgnoreCase("PDF")) {
	        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\".audit-logs.pdf\"");
	        auditService.exportAuditLogsToPdf(response.getOutputStream(), requestMap);
	    }
	}
	
	@GetMapping("{logId}")
	public ResponseEntity<?> getAuditLog(@PathVariable(name = ICommonConstants.LOG_ID) Long logId) {
		return ResponseEntity.ok(auditService.getAuditLogById(logId));
	}
}
