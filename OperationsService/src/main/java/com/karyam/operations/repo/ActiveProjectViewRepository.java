package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.ActiveProjectView;

public interface ActiveProjectViewRepository extends JpaRepository<ActiveProjectView, Long> {

}
