package com.karyam.operations.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class VendorListResponse {

	private List<VendorResponse> vendors;
	private int number;
	private int totalPages;
}
