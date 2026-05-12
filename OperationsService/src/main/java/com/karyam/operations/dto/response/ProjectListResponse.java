package com.karyam.operations.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class ProjectListResponse {

	private List<ProjectResponse> projects;
	private int number;
	private int totalPages;
}
