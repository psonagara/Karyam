package com.karyam.operations.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class PayrollListResponse {

	private List<PayrollResponse> payrolls;
	private int number;
	private int totalPages;
}
