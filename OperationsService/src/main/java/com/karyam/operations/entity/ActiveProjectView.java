package com.karyam.operations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "vw_active_projects")
public class ActiveProjectView {

	@Id
    private Long id;
    
    @Column(name = "project_id")
    private String projectId;
    
    private String name;
    
    private String location;
    
    private BigDecimal budget;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    private String manager;
    
    private String status;
    
    @Column(name = "total_workers")
    private Long totalWorkers;
    
    @Column(name = "active_workers")
    private Long activeWorkers;
}
