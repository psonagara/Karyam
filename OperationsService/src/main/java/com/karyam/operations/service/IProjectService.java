package com.karyam.operations.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.ProjectRequest;
import com.karyam.operations.dto.response.ProjectListResponse;
import com.karyam.operations.dto.response.ProjectResponse;

public interface IProjectService {

	String createProject(ProjectRequest request, RequestMetadata data);
	ProjectListResponse getAllProject(Map<String, Object> requestMa, Pageable pageablep);
	ProjectResponse getProjectById(Long id);
	String updateProject(Long projectId, ProjectRequest request, RequestMetadata data);
	String deleteProjectById(Long projectId, RequestMetadata data);
	List<?> getProjectNames();
}
