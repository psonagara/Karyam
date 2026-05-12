package com.karyam.audit.repo;

import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.karyam.audit.entity.AuditLog;

import jakarta.persistence.QueryHint;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

	@Query("""
			  SELECT a FROM AuditLog a WHERE
			     (:userId IS NULL OR a.user.id = :userId) AND
			     (:action IS NULL OR a.action = :action) AND
			     (:entity IS NULL OR a.entity = :entity) AND
			     (:startDate IS NULL OR a.timestamp >= :startDate) AND
			     (:search IS NULL OR (
			        LOWER(a.userName) LIKE LOWER(CONCAT('%', :search, '%')) OR
			        LOWER(a.details) LIKE LOWER(CONCAT('%', :search, '%')) OR
			        LOWER(a.action) LIKE LOWER(CONCAT('%', :search, '%'))
			     ))
			""")
	Page<AuditLog> searchAuditLogs(
			@Param("userId") Long userId,
			@Param("action") String action,
			@Param("entity") String entity,
			@Param("startDate") LocalDateTime startDate,
			@Param("search") String search,
			Pageable pageable);

	long countByTimestampGreaterThanEqual(LocalDateTime timestamp);

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "50"))
	@Query("""
			 SELECT a FROM AuditLog a WHERE
			     (:action IS NULL OR a.action = :action) AND
			     (:entity IS NULL OR a.entity = :entity) AND
			     (a.timestamp >= :startDate)
			""")
	Stream<AuditLog> streamAllByFilters(
			@Param("action") String action,
			@Param("entity") String entity,
			@Param("startDate") LocalDateTime startDate);
}
