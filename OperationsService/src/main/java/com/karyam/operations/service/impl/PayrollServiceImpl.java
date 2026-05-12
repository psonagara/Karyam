package com.karyam.operations.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.PayrollDTO;
import com.karyam.event.dto.PayrollPaidDTO;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.PayrollDashboardStats;
import com.karyam.operations.dto.PayrollProjection;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.PayrollRequest;
import com.karyam.operations.dto.response.PayrollListResponse;
import com.karyam.operations.dto.response.PayrollMappingHelper;
import com.karyam.operations.dto.response.PayrollResponse;
import com.karyam.operations.entity.Payroll;
import com.karyam.operations.enu.PayrollStatus;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.repo.PayrollRepository;
import com.karyam.operations.service.IPayrollService;
import com.karyam.operations.util.JwtUtil;
import com.opencsv.CSVWriter;

import jakarta.transaction.Transactional;

@Service
public class PayrollServiceImpl implements IPayrollService {
	
	@Autowired
	private PayrollRepository payrollRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;

	@Override
	public Map<String, Object> generatePayroll(Map<String, Object> requestMap, RequestMetadata data) {
		Integer month = (Integer) requestMap.get(ICommonConstants.MONTH);
		Integer year = (Integer) requestMap.get(ICommonConstants.YEAR);
		Long projectId = (Long) requestMap.get(ICommonConstants.PROJECT_ID);
		
		YearMonth yearMonth = YearMonth.of(year, month);
		
		List<PayrollProjection> payrollProjections = payrollRepository
				.callGeneratePayroll(projectId, yearMonth.toString(), String.valueOf(JwtUtil.getUserId()));
		List<PayrollDTO> payrolls = payrollProjections
				.stream()
				.map(PayrollMappingHelper::toPayrollDTO)
				.collect(Collectors.toList());
		KafkaEvent<?> payrollEvent = PayrollMappingHelper.createPayrollEvent(data, "payroll.generated", null, payrolls);
		kafkaProducer.publishLaborEvent(payrollEvent);
		
		Map<String, Object> response = new HashMap<>();
		response.put("count", payrolls.size());
		response.put("payroll", payrolls);
		return response;
	}

	@Override
	public PayrollListResponse filterPayrolls(Map<String, Object> requestMap, Pageable pageable) {
		
		Integer month = (Integer) requestMap.get(ICommonConstants.MONTH);
		Integer year = (Integer) requestMap.get(ICommonConstants.YEAR);
		String monthStr = month != null ? String.format("%02d", month) : null;
		String yearStr = year != null ? String.valueOf(year) : null;
		
		Page<Payroll> pages = payrollRepository.filterPayroll(
				(Long) requestMap.get(ICommonConstants.PROJECT_ID), 
				(PayrollStatus) requestMap.get(ICommonConstants.STATUS), 
				monthStr, 
				yearStr, 
				(String) requestMap.get(ICommonConstants.SEARCH), 
				pageable);
		
		List<PayrollResponse> payrollList = pages.getContent()
				.stream()
				.map(PayrollMappingHelper::toPayrollResponse)
				.collect(Collectors.toList());
		PayrollListResponse response = new PayrollListResponse();
		response.setPayrolls(payrollList);
		response.setNumber(pages.getNumber());
		response.setTotalPages(pages.getTotalPages());
		return response;
	}

	@Override
	public PayrollResponse getPayrollById(Long payrollId) {
		Payroll payroll = payrollRepository.findById(payrollId)
				.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.PAYROLL_NOT_FOUND));
		return PayrollMappingHelper.toPayrollResponse(payroll);
	}

	@Override
	public String markPayrollPaid(Long payrollId, Map<String, Object> requestMap, RequestMetadata data) {
		
		String paymentMethod = (String) requestMap.get("paymentMethod");
		Long paid = payrollRepository.markPayrollPaid(JwtUtil.getUserId(), payrollId, paymentMethod);
		if (paid < 1) {
			throw new InternalServerException(IExceptionConstants.PAYROLL_PAID_FAIL);
		}
		
		PayrollPaidDTO dto = new PayrollPaidDTO(payrollId, paymentMethod);
		KafkaEvent<?> payrollEvent = PayrollMappingHelper.createPayrollEvent(data, "payroll.paid", null, dto);
		kafkaProducer.publishLaborEvent(payrollEvent);
		
		return String.format(IResponseConstants.PAYROLL_PAID_MARKED, paid);
	}

	@Override
	public Map<String, Object> markAllPayrollPaid(PayrollRequest request, RequestMetadata data) {
		YearMonth yearMonth = YearMonth.of(request.getYear(), request.getMonth());
		String monthYear = yearMonth.toString();
		Object[] payrollPaid = payrollRepository.markAllPayrollPaid(request.getProjectId(), 
				monthYear, 
				JwtUtil.getUserId(), 
				request.getPaymentMethod());
		
		PayrollPaidDTO dto = new PayrollPaidDTO(request.getProjectId(), request.getPaymentMethod());
		KafkaEvent<?> payrollEvent = PayrollMappingHelper.createPayrollEvent(data, "payroll.paid.all", null, dto);
		kafkaProducer.publishLaborEvent(payrollEvent);
		
		return Map.of("count", payrollPaid[0]);
	}

	@Override
	public PayrollDashboardStats getPayrollStats() {
		YearMonth yearMonth = YearMonth.now();
		return payrollRepository.getFormattedStats(yearMonth.toString());
	}

	@Override
	@Transactional
	public void exportPayrollToCsv(PrintWriter writer, Map<String, Object> requestMap) {
		Integer month = (Integer) requestMap.get(ICommonConstants.MONTH);
		String monthStr = (month != -1) ? String.format("%02d", month) : "";
		
		try (CSVWriter csvWriter = new CSVWriter(writer)) {

			String[] header = {"Labor ID", "Name", "Type", "Daily Wage", "Present Days", "Overtime Hours", "Basic Salary", "Overtime Pay", "Total Salary", "Status"};
			csvWriter.writeNext(header);

			try (Stream<Payroll> stream = 
					payrollRepository.streamAllByFilters(
							(Long) requestMap.get(ICommonConstants.PROJECT_ID), 
							month,
							monthStr,
							(Integer) requestMap.get(ICommonConstants.YEAR)
							)) {

				stream.forEach(payroll -> {
					csvWriter.writeNext(new String[] {
							payroll.getLabor().getLaborId(),
							payroll.getLabor().getName(),
							payroll.getLaborType(),
							String.valueOf(payroll.getDailyWage()),
							String.valueOf(payroll.getPresentDays()),
							String.valueOf(payroll.getOvertimeHours()),
							String.valueOf(payroll.getBasicSalary()),
							String.valueOf(payroll.getOvertimePay()),
							String.valueOf(payroll.getTotalSalary()),
							String.valueOf(payroll.getStatus())
							
					});
				});
			}
		} catch (IOException e) {
			throw new InternalServerException(IExceptionConstants.CSV_GENERATION_FAIL);
		}
	}
	
}
