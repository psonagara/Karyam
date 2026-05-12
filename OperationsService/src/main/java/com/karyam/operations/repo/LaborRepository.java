package com.karyam.operations.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.karyam.operations.dto.WorkerAttendanceProjection;
import com.karyam.operations.entity.Labor;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.LaborType;

public interface LaborRepository extends JpaRepository<Labor, Long> {

	boolean existsByPhone(String phone);

	@Query("""
		    SELECT l FROM Labor l 
		    LEFT JOIN l.assignedProject p
		    WHERE
		    (:status IS NULL OR l.status = :status) AND
		    (:type IS NULL OR l.laborType = :type) AND
		    (
		        (:showUnassigned = true AND p IS NULL) 
		        OR 
		        (:showUnassigned = false AND :projectId IS NOT NULL AND p.id = :projectId)
		        OR
		        (:showUnassigned = false AND :projectId IS NULL)
		    ) AND
		    (:search IS NULL OR :search = '' OR
		    LOWER(l.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
		    LOWER(l.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR
		    LOWER(l.laborId) LIKE LOWER(CONCAT('%', :search, '%')))
		    """)
	Page<Labor> filterLabors(@Param("status") ActivationStatus status, @Param("search") String search,
			@Param("projectId") Long projectId, @Param("type") LaborType laborType, 
			@Param("showUnassigned") boolean unAssigned, Pageable pageable);
	
	long countByStatus(ActivationStatus status);
	
	@Query("SELECT AVG(l.dailyWage) FROM Labor l WHERE l.status = 'ACTIVE'")
    Double findAverageDailyWage();
	
	Optional<Labor> findByLaborId(String laborId);
	
	@Query(value = "CALL sp_get_workers_for_attendance(:date, :projectId, :laborType)", nativeQuery = true)
	List<WorkerAttendanceProjection> getWorkersForAttendance(@Param("date") LocalDate date,
			@Param("projectId") Long projectId, @Param("laborType") LaborType type);
}
