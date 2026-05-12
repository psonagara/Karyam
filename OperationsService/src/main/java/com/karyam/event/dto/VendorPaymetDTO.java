package com.karyam.event.dto;

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
public class VendorPaymetDTO {

	private Long id;
	private String paymentId;
	private Long vendorId;
	private BigDecimal amount;
	private LocalDate paymentDate;
	private String paymentMethod;
	private String referenceNumber;
	private String remarks;
}
