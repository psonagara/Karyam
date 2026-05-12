package com.karyam.operations.repo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.karyam.operations.entity.Expense;
import com.karyam.operations.enu.ExpenseCategory;
import com.karyam.operations.enu.ExpenseStatus;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	@Query("""
		    SELECT e FROM Expense e WHERE
		    (:status IS NULL OR e.status = :status) AND
		    (:category IS NULL OR e.category = :category) AND
		    (:projectId IS NULL OR e.project.id = :projectId) AND
		    (:search IS NULL OR :search = '' OR
		        LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')) OR
		        LOWER(e.billNumber) LIKE LOWER(CONCAT('%', :search, '%'))
		    )
		""")
	Page<Expense> filterExpenses(@Param("category") ExpenseCategory category, 
		    @Param("search") String search, @Param("projectId") Long projectId,
		    @Param("status") ExpenseStatus status, Pageable pageable);

	@Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
	BigDecimal findTotalExpenseAmount();

	@Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.status = 'APPROVED'")
	BigDecimal findTotalApprovedAmount();

	@Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.status = 'PENDING'")
	BigDecimal findTotalPendingAmount();
	
	Optional<Expense> findByIdAndStatus(Long id, ExpenseStatus status);
	
	long countByStatus(ExpenseStatus status);
	
	// TODO: correct this status
	long countByStatusAndDate(ExpenseStatus status, LocalDate date);
	
	@Query(value = "CALL sp_approve_expense(:expenseId, :approvedBy)", nativeQuery = true)
	Object[] approveExpense(@Param("expenseId") Long expenseId, @Param("approvedBy") Long approvedBy);
	
	@Query(value = "CALL sp_reject_expense(:expenseId, :rejectedBy, :reason)", nativeQuery = true)
	Object[] rejectExpense(@Param("expenseId") Long expenseId, @Param("rejectedBy") Long rejectedBY, @Param("reason") String reason);
}
