package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.PayrollSummaryView;

public interface PayrollSummaryViewRepository extends JpaRepository<PayrollSummaryView, Long> {

}
