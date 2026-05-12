package com.karyam.audit.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.karyam.audit.enu.AuditStatus;
import com.karyam.audit.enu.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_timestamp", columnList = "timestamp"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_action", columnList = "action"),
    @Index(name = "idx_entity", columnList = "entity"),
    @Index(name = "idx_entity_id", columnList = "entity_id"),
    @Index(name = "idx_entity_composite", columnList = "entity, entity_id"),
    @Index(name = "idx_user_activity", columnList = "user_id, timestamp"),
    @Index(name = "idx_action_time", columnList = "action, timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String eventId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, precision = 3)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_audit_logs_user"))
    private User user;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "user_role", length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(nullable = false, length = 50)
    private String entity;

    @Column(name = "entity_id", length = 50)
    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "old_value", columnDefinition = "JSON")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "JSON")
    private String newValue;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditStatus status = AuditStatus.SUCCESS;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
