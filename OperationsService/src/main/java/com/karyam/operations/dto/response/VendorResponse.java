package com.karyam.operations.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.VendorCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponse {
	
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private VendorCategory category;
    private String paymentTerms;
    private String address;
    private BigDecimal dueAmount;
    private ActivationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
