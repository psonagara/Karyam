package com.karyam.operations.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorPaymentRequest {

	private BigDecimal amount;
	private LocalDate paymentDate;
	private String paymentMethod;
	private String referenceNumber;
	private String remarks;
}
