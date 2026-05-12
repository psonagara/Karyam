package com.karyam.operations.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class ExpenseListResponse {

	private List<ExpenseResponse> expenses;
	private int number;
	private int totalPages;
}
