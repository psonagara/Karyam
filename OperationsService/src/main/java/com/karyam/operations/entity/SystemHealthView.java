package com.karyam.operations.entity;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_system_health")
@Getter
public class SystemHealthView {

    @Id
    @Column(name = "active_projects")
    private Long activeProjects;

    @Column(name = "active_workers")
    private Long activeWorkers;

    @Column(name = "pending_expenses")
    private Long pendingExpenses;

    @Column(name = "pending_payroll")
    private Long pendingPayroll;

    @Column(name = "total_vendor_dues", precision = 15, scale = 2)
    private BigDecimal totalVendorDues;

    @Column(name = "actions_last_24h")
    private Long actionsLast24h;

    @Column(name = "active_users_today")
    private Long activeUsersToday;
}
