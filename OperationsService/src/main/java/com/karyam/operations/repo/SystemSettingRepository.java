package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.SystemSetting;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {

}
