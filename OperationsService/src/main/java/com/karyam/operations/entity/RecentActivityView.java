package com.karyam.operations.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_recent_activity")
@Getter
public class RecentActivityView {

    @Id
    private Long id;

    private LocalDateTime timestamp;

    @Column(name = "user_name")
    private String userName;

    private String action;

    private String entity;

    @Column(name = "entity_id")
    private String entityId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "entity_name")
    private String entityName;
}
