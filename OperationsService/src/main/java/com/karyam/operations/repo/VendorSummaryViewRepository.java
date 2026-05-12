package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.VendorSummaryView;

public interface VendorSummaryViewRepository extends JpaRepository<VendorSummaryView, Long> {

}
