package com.karyam.operations.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PendingApprovalProjection {
    Long getId();
    String getExpenseId();
    Long getProjectId();
    String getProjectName();
    Long getVendorId();
    String getVendorName();
    String getCategory();
    BigDecimal getAmount();
    LocalDate getDate();
    String getDescription();
    String getBillNumber();
    String getRequestedBy();
    Long getRequestedById();
    LocalDateTime getCreatedAt();
    Integer getDaysPending();
}