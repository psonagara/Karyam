package com.karyam.operations.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.karyam.operations.dto.BudgetAlertDTO;
import com.karyam.operations.dto.DashboardStatsDTO;
import com.karyam.operations.entity.Project;
import com.karyam.operations.enu.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	boolean existsByName(String name);
	
	@Query("""
			SELECT p FROM Project p WHERE
		    (:status IS NULL OR p.status = :status) AND
		    (:search IS NULL OR :search = '' OR
		    LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
		    LOWER(p.location) LIKE LOWER(CONCAT('%', :search, '%')) OR
		    LOWER(p.manager) LIKE LOWER(CONCAT('%', :search, '%')))
		    """)
	Page<Project> filterProjects(@Param("status") ProjectStatus status, @Param("search") String search, Pageable pageable);
	
	Optional<Project> findByProjectId(String projectId);
	
	List<ProjectName> findAllProjectBy();
	
	public interface ProjectName {
		String getId();
		String getName();
	}
	
	@Query(value = "CALL sp_get_dashboard_stats()", nativeQuery = true)
	DashboardStatsDTO findDashboardStats();
	
	@Query(value = "CALL sp_get_budget_alerts()", nativeQuery = true)
    List<BudgetAlertDTO> getBudgetAlerts();
}


