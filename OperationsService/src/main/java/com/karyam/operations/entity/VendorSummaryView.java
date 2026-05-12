package com.karyam.operations.entity;

import java.math.BigDecimal;

import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.VendorCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "vw_vedor_summary")
public class VendorSummaryView {

	@Id
    private Long id;

    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private VendorCategory category;

    @Column(name = "payment_terms")
    private String paymentTerms;

    @Column(name = "due_amount", precision = 15, scale = 2)
    private BigDecimal dueAmount;

    @Enumerated(EnumType.STRING)
    private ActivationStatus status;

    @Column(name = "total_expenses")
    private Long totalExpenses;

    @Column(name = "total_approved_expenses", precision = 15, scale = 2)
    private BigDecimal totalApprovedExpenses;

    @Column(name = "total_payments")
    private Long totalPayments;

    @Column(name = "total_paid", precision = 15, scale = 2)
    private BigDecimal totalPaid;
}
