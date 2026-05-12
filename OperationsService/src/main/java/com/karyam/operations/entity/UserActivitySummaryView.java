package com.karyam.operations.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_user_activity_summary")
@Getter
public class UserActivitySummaryView {

    @Id
    private Long id;

    private String name;

    private String email;

    private String role;

    @Column(name = "total_actions")
    private Long totalActions;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "create_count")
    private Long createCount;

    @Column(name = "update_count")
    private Long updateCount;

    @Column(name = "delete_count")
    private Long deleteCount;
}
