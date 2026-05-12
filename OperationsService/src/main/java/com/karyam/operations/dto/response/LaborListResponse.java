package com.karyam.operations.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class LaborListResponse {

	private List<LaborResponse> labors;
	private int number;
	private int totalPages;
}
