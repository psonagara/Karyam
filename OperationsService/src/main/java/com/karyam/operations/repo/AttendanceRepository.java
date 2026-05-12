package com.karyam.operations.repo;

import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.karyam.operations.dto.AttendanceSummaryProjection;
import com.karyam.operations.entity.Attendance;

import jakarta.persistence.QueryHint;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	@Query(value = "CALL sp_get_attendance_summary(:date)", nativeQuery = true)
    AttendanceSummaryProjection getAttendanceSummary(@Param("date") LocalDate date);

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "50"))
    @Query("""
        SELECT a FROM Attendance a 
        LEFT JOIN FETCH a.labor l
        WHERE (:projectId IS NULL OR l.assignedProject.id = :projectId)
        AND (:startDate IS NULL OR a.date >= :startDate)
        AND (:endDate IS NULL OR a.date <= :endDate)
        ORDER BY a.date DESC
        """)
	Stream<Attendance> streamAllByFilters(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("projectId") Long projectId);
}
