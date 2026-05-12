package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.RecentActivityView;

public interface RecentActivityViewRepository extends JpaRepository<RecentActivityView, Long>{

}
