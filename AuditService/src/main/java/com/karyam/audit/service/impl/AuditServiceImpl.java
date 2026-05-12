package com.karyam.audit.service.impl;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.karyam.audit.constant.ICommonConstants;
import com.karyam.audit.constant.IExceptionConstants;
import com.karyam.audit.dto.response.AuditListResponse;
import com.karyam.audit.dto.response.AuditResponse;
import com.karyam.audit.entity.AuditLog;
import com.karyam.audit.entity.User;
import com.karyam.audit.enu.ActivationStatus;
import com.karyam.audit.exception.InternalServerException;
import com.karyam.audit.exception.ResourceNotFoundException;
import com.karyam.audit.helper.AuditMappingHelper;
import com.karyam.audit.repo.AuditLogRepository;
import com.karyam.audit.repo.UserRepository;
import com.karyam.audit.service.IAuditService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;

import jakarta.transaction.Transactional;

@Service
public class AuditServiceImpl implements IAuditService {

	@Autowired
	private AuditLogRepository auditLogRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void saveAuditLog(AuditLog auditLog) {
		try {
			auditLogRepository.save(auditLog);
		} catch (Exception e) {
			throw new RuntimeException("Audit Log save failed");
		}
	}

	@Override
	public void saveAllAuditLogs(List<AuditLog> auditLogs) {
		try {
			auditLogRepository.saveAll(auditLogs);
		} catch (Exception e) {
			throw new RuntimeException("Audit Log save failed");
		}
	}

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).get();
	}

	@Override
	public AuditListResponse filterAuditLogs(Map<String, Object> requestMap, Pageable pageable) {
		String search = (String) requestMap.get(ICommonConstants.SEARCH);
		String action = (String) requestMap.get(ICommonConstants.ACTION);
		String entity = (String) requestMap.get(ICommonConstants.ENTITY);
		Long userId = (Long) requestMap.get(ICommonConstants.USER_ID);
		Long days = (Long) requestMap.get(ICommonConstants.DAYS);

		LocalDateTime startDate = null;
		if (days != null && days > 0) {
			startDate = LocalDateTime.now().minusDays(days);
		}
		Page<AuditLog> pages = auditLogRepository.searchAuditLogs(userId, action, entity, startDate, search, pageable);
		List<AuditResponse> auditLogs = pages.getContent()
				.stream()
				.map(AuditMappingHelper::toAuditResponse)
				.collect(Collectors.toList());

		AuditListResponse auditListResponse = new AuditListResponse();
		auditListResponse.setAudits(auditLogs);
		auditListResponse.setNumber(pages.getNumber());
		auditListResponse.setTotalPages(pages.getTotalPages());
		return auditListResponse;
	}

	@Override
	public Map<String, Object> getAuditStats() {
		long totalActivities = auditLogRepository.count();
		long todayActivities = auditLogRepository.countByTimestampGreaterThanEqual(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
		long activeUsers = userRepository.countByIsActive(ActivationStatus.ACTIVE);

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("totalActivities", totalActivities);
		responseMap.put("todayActivities", todayActivities);
		responseMap.put("activeUsers", activeUsers);
		return responseMap;
	}

	@Override
	@Transactional
	public void exportAuditLogsToCsv(PrintWriter writer, Map<String, Object> requestMap) {
		try(CSVWriter csvWriter = new CSVWriter(writer)) {

			String[] header = {"Timestamp", "User", "Role", "Action", "Entity Id", "Entity", "Details"};
			csvWriter.writeNext(header);

			String action = (String) requestMap.get(ICommonConstants.ACTION);
			String entity = (String) requestMap.get(ICommonConstants.ENTITY);
			Long days = (Long) requestMap.get(ICommonConstants.DAYS);
			LocalDateTime startDate = LocalDateTime.now().minusDays(days);

			try (Stream<AuditLog> stream = auditLogRepository.streamAllByFilters(action, entity, startDate)) {

				stream.forEach(record -> {
					csvWriter.writeNext(new String[] {
							String.valueOf(record.getTimestamp()),
							record.getUserName(),
							record.getUserRole().toString(),
							record.getAction(),
							String.valueOf(record.getEntityId()),
							record.getEntity(),
							record.getDetails()
					});
				});
			}

		} catch (IOException e) {
			throw new InternalServerException(IExceptionConstants.CSV_GENERATION_FAIL);
		}
	}

	@Override
	@Transactional
	public void exportAuditLogsToJson(OutputStream outputStream, Map<String, Object> requestMap) {

		String action = (String) requestMap.get(ICommonConstants.ACTION);
		String entity = (String) requestMap.get(ICommonConstants.ENTITY);
		Long days = (Long) requestMap.get(ICommonConstants.DAYS);
		LocalDateTime startDate = LocalDateTime.now().minusDays(days);

		try (Stream<AuditLog> stream = auditLogRepository.streamAllByFilters(action, entity, startDate)) {

			try (SequenceWriter writer = objectMapper.writer().withDefaultPrettyPrinter().writeValues(outputStream)) {

				writer.init(true);
				stream.forEach(record -> {
					try {
						writer.write(AuditMappingHelper.toAuditResponse(record));
					} catch (IOException e) {
						throw new InternalServerException(IExceptionConstants.JSON_RECORD_WRITING_FAIL);
					}
				});
			}
		} catch (IOException e) {
			throw new InternalServerException(IExceptionConstants.JSON_GENERATION_FAIL);
		}
	}

	@Override
	@Transactional
	public void exportAuditLogsToPdf(OutputStream outputStream, Map<String, Object> requestMap) {
		Document document = new Document(PageSize.A4.rotate());
		try {
			PdfWriter.getInstance(document, outputStream);
			document.open();

			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			fontTitle.setSize(18);
			Paragraph paragraph = new Paragraph("Audit Log Report", fontTitle);
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(paragraph);
			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100);
			table.setWidths(new float[] {2.5f, 2f, 2f, 2f, 2f, 2f, 4f});

			Stream.of("Timestamp", "User", "Role", "Action", "Entity Id", "Entity", "Details")
			.forEach(headerTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(Color.LIGHT_GRAY);
				header.setPhrase(new Phrase(headerTitle));
				table.addCell(header);
			});

			String action = (String) requestMap.get(ICommonConstants.ACTION);
			String entity = (String) requestMap.get(ICommonConstants.ENTITY);
			Long days = (Long) requestMap.get(ICommonConstants.DAYS);
			LocalDateTime startDate = LocalDateTime.now().minusDays(days);

			try (Stream<AuditLog> stream = auditLogRepository.streamAllByFilters(action, entity, startDate)) {

				stream.forEach(record -> {
					table.addCell(record.getTimestamp().toString());
					table.addCell(record.getUserName());
					table.addCell(record.getUserRole().toString());
					table.addCell(record.getAction());
					table.addCell(String.valueOf(record.getEntityId()));
					table.addCell(record.getEntity());
					table.addCell(record.getDetails());
				});
			}

			document.add(table);
			document.close();
		} catch (DocumentException e) {
			throw new InternalServerException(IExceptionConstants.PDF_GENERATION_FAIL);
		}

	}

	@Override
	public AuditResponse getAuditLogById(Long logId) {
		AuditLog auditLog = auditLogRepository.findById(logId)
				.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.AUDIT_LOG_NOT_FOUND));
		return AuditMappingHelper.toAuditResponse(auditLog);
	}
}
