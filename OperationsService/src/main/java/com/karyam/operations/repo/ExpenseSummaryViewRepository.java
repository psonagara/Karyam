package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.ExpenseSummaryView;

public interface ExpenseSummaryViewRepository extends JpaRepository<ExpenseSummaryView, Long> {

}
