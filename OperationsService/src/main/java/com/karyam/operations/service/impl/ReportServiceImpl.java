package com.karyam.operations.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ProjectStatus;
import com.karyam.operations.enu.VendorCategory;
import com.karyam.operations.service.IReportService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class ReportServiceImpl implements IReportService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Map<String, Object> getExpenseReport(Map<String, Object> requestMap) {
		
		Integer days = (Integer) requestMap.get(ICommonConstants.DAYS);
		days = days == null ? 30 : days;
		Long projectId = (Long) requestMap.get(ICommonConstants.PROJECT_ID); 
		ExpenseCategory eCategory = (ExpenseCategory) requestMap.get(ICommonConstants.STATUS);
		String category = eCategory != null ? eCategory.toString() : null;
		
		
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_expense_report");
		
		query.registerStoredProcedureParameter(0, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		
		query.setParameter(0, days);
		query.setParameter(1, projectId);
		query.setParameter(2, category);
		
		Map<String, Object> report = new HashMap<>();
		
		query.execute();
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
            Object[] row = resultList.get(0);
            report.put("summary", Map.of(
                "total", row[0],
                "approved", row[1],
                "pending", row[2]
            ));
        }
		
		if (query.hasMoreResults()) {
            report.put("trend", query.getResultList());
        }
		if (query.hasMoreResults()) {
            report.put("categories", query.getResultList());
        }
		return report;
	}
	
	@Override
    public Map<String, Object> getPayrollReport(Map<String, Object> requestMap) {
		
		String month = (String) requestMap.get(ICommonConstants.MONTH);
		Long projectId = (Long) requestMap.get(ICommonConstants.PROJECT_ID);
		
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_payroll_report");

        query.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_project_id", Long.class, ParameterMode.IN);

        query.setParameter("p_month", month);
        query.setParameter("p_project_id", projectId);

        Map<String, Object> response = new HashMap<>();

        query.execute();
        @SuppressWarnings("unchecked")
		List<Object[]> summaryList = query.getResultList();
        if (!summaryList.isEmpty()) {
            Object[] row = summaryList.get(0);
            response.put("total", row[0]);
            response.put("workers", row[1]);
            response.put("average", row[2]);
        }

        if (query.hasMoreResults()) {
            response.put("byType", query.getResultList());
        }
        return response;
    }
	
	@Override
    public Map<String, Object> getVendorReport(Map<String, Object> requestMap) {
		
		VendorCategory vCategory = (VendorCategory) requestMap.get(ICommonConstants.CATEGORY);
		String category = vCategory != null ? vCategory.toString() : null;
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_vendor_report");

        query.registerStoredProcedureParameter("p_category", String.class, ParameterMode.IN);
        
        query.setParameter("p_category", category);

        Map<String, Object> result = new HashMap<>();

        query.execute();
        @SuppressWarnings("unchecked")
		List<Object[]> summaryList = query.getResultList();
        if (!summaryList.isEmpty()) {
            Object[] row = summaryList.get(0);
            result.put("totalVendors", row[0]);
            result.put("totalDues", row[1]);
        }

        if (query.hasMoreResults()) {
            result.put("topVendors", query.getResultList());
        }
        return result;
    }
	
	
	@Override
    public Map<String, Object> getProjectReport(Map<String, Object> requestMap) {
		
		ProjectStatus pStatus = (ProjectStatus) requestMap.get(ICommonConstants.STATUS);
		String status = pStatus != null ? pStatus.toString() : null;
		
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_project_report");

        query.registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN);
        
        query.setParameter("p_status", (status == null || status.isEmpty()) ? null : status);

        Map<String, Object> result = new HashMap<>();

        query.execute();
        @SuppressWarnings("unchecked")
		List<Object[]> summaryList = query.getResultList();
        if (!summaryList.isEmpty()) {
            Object[] row = summaryList.get(0);
            result.put("totalProjects", row[0]);
            result.put("totalBudget", row[1]);
            result.put("totalSpent", row[2]);
        }

        if (query.hasMoreResults()) {
            result.put("projectBreakdown", query.getResultList());
        }
        return result;
    }

}
