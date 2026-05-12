package com.karyam.operations.repo;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.karyam.operations.dto.PayrollDashboardStats;
import com.karyam.operations.dto.PayrollProjection;
import com.karyam.operations.entity.Payroll;
import com.karyam.operations.enu.PayrollStatus;

import jakarta.persistence.QueryHint;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

	@Query(value = "CALL sp_generate_payroll(:projectId, :month, :generatedBy)", nativeQuery = true)
	List<PayrollProjection> callGeneratePayroll(
			@Param("projectId") Long projectId,
			@Param("month") String yearMonth,
			@Param("generatedBy") String generatedBy
			);

	@Query("""
			    SELECT p FROM Payroll p WHERE
			    (:projectId IS NULL OR p.project.id = :projectId) AND
			    (:status IS NULL OR p.status = :status) AND
			    (:search IS NULL OR :search = '' OR 
			        LOWER(p.payrollId) LIKE LOWER(CONCAT('%', :search, '%')) OR 
			        LOWER(p.labor.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND
			    (:month IS NULL OR p.month LIKE CONCAT('%-', :month)) AND
			    (:year IS NULL OR p.month LIKE CONCAT(:year, '-%'))
			""")
	Page<Payroll> filterPayroll(
			@Param("projectId") Long projectId,
			@Param("status") PayrollStatus status, 
			@Param("month") String monthStr,
			@Param("year") String yearStr,
			@Param("search") String search,
			Pageable pageable);

	@Query(value = "CALL sp_mark_payroll_paid(:id, :paidBy, :method)", nativeQuery = true)
	Long markPayrollPaid(@Param("paidBy") Long paidBy, @Param("id") Long payrollId, @Param("method") String paymentMethos);

	@Query(value = "CALL sp_mark_all_payroll_paid(:projectId, :month, :paidBy, :method)", nativeQuery = true)
	Object[] markAllPayrollPaid(@Param("projectId") Long projectId, @Param("month") String monthYear, 
			@Param("paidBy") Long paidBy, @Param("method") String paymentMethod);

	@Query("""
			    SELECT new com.karyam.operations.dto.PayrollDashboardStats(
			        COALESCE(SUM(p.totalSalary), 0),
			        COALESCE(SUM(CASE WHEN p.status = 'PENDING' THEN p.totalSalary ELSE 0 END), 0),
			        COALESCE(SUM(CASE WHEN p.status = 'PAID' AND p.month = :currentMonth THEN p.totalSalary ELSE 0 END), 0),
			        COUNT(DISTINCT p.labor.id)
			    )
			    FROM Payroll p
			""")
	PayrollDashboardStats getFormattedStats(@Param("currentMonth") String currentMonth);

	@QueryHints(value = @QueryHint(name = "org.hibernate.fetchSize", value = "50"))
	@Query("""
			    SELECT p FROM Payroll p
			    WHERE p.project.id = :projectId 
			    AND p.month LIKE CONCAT(:year, '-%')
			    AND (:month = -1 OR p.month LIKE CONCAT('%-', :monthStr))
			""")
	Stream<Payroll> streamAllByFilters(
			@Param("projectId") Long projectId, 
			@Param("month") Integer month, 
			@Param("monthStr") String monthStr,
			@Param("year") Integer year
			);
}
