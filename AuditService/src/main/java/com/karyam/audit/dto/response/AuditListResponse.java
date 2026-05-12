package com.karyam.audit.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class AuditListResponse {

	private List<AuditResponse> audits;
	private int number;
	private int totalPages;
}
