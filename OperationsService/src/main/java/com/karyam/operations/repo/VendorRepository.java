package com.karyam.operations.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.karyam.operations.entity.Vendor;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.VendorCategory;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

	boolean existsByPhoneOrEmail(String phone, String email);
	
	
	@Query("""
			SELECT v FROM Vendor v WHERE
		    (:status IS NULL OR v.status = :status) AND
		    (:category IS NULL OR v.category = :category) AND
		    (:search IS NULL OR :search = '' OR
		    LOWER(v.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
		    LOWER(v.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR
		    LOWER(v.contactPerson) LIKE LOWER(CONCAT('%', :search, '%')))
		    """)
	Page<Vendor> filterVendors(@Param("category") VendorCategory category, 
			@Param("search") String search, 
			@Param("status") ActivationStatus status, 
			Pageable pageable);
	
	 @Query("SELECT COALESCE(SUM(v.dueAmount), 0) FROM Vendor v")
	 BigDecimal findTotalDueAmount();

	 long countByStatus(ActivationStatus status);

	 List<VendorName> findAllVendorBy();

	 public interface VendorName {
		 String getId();
		 String getName();
	 }
}
