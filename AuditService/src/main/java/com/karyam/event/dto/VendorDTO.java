package com.karyam.event.dto;

import java.math.BigDecimal;

import com.karyam.audit.enu.ActivationStatus;
import com.karyam.audit.enu.VendorCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorDTO {

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
}
