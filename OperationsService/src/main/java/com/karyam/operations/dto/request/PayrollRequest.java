package com.karyam.operations.dto.request;

import lombok.Data;

@Data
public class PayrollRequest {

	private String paymentMethod;
	private Long projectId;
	private Integer month;
	private Integer year;
}
