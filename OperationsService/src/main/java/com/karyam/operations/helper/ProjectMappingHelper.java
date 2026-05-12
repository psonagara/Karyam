package com.karyam.operations.helper;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.ProjectDTO;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.ProjectRequest;
import com.karyam.operations.dto.response.ProjectResponse;
import com.karyam.operations.entity.Project;
import com.karyam.operations.util.CommonUtil;

public interface ProjectMappingHelper {

	public static Project toProject(ProjectRequest request) {
		Project project = Project.builder()
				.name(request.getName())
				.location(request.getLocation())
				.description(request.getDescription())
				.budget(request.getBudget())
				.currency(request.getCurrency())
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.manager(request.getManager())
				.status(request.getStatus())
				.contactPerson(request.getContactPerson())
				.contactPhone(request.getContactPhone())
				.contactEmail(request.getContactEmail())
				.build();
		return project;
	}
	
	public static ProjectDTO toProjectDTO(Project project) {
		return ProjectDTO.builder()
				.projectId(project.getId())
				.projectDisplayId(project.getProjectId())
				.projectName(project.getName())
				.location(project.getLocation())
				.budget(project.getBudget())
				.currency(project.getCurrency())
				.startDate(project.getStartDate())
				.endDate(project.getEndDate())
				.status(project.getStatus())
				.description(project.getDescription())
				.manager(project.getManager())
				.contactPerson(project.getContactPerson())
				.contactPhone(project.getContactPhone())
				.contactEmail(project.getContactEmail())
				.build();
	}
	
	public static KafkaEvent<?> createProjectEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Project", oldValue, newValue);
	}
	
	public static ProjectResponse toProjectResponse(Project project) {
		return ProjectResponse.builder()
				.id(project.getId())
				.projectId(project.getProjectId())
				.name(project.getName())
				.location(project.getLocation())
				.description(project.getDescription())
				.budget(project.getBudget())
				.currency(project.getCurrency())
				.startDate(project.getStartDate())
				.endDate(project.getEndDate())
				.manager(project.getManager())
				.status(project.getStatus())
				.contactPerson(project.getContactPerson())
				.contactPhone(project.getContactPhone())
				.contactEmail(project.getContactEmail())
				.createdAt(project.getCreatedAt())
				.updateAt(project.getUpdateAt())
				.build();
	}
	
	public static void updateProject(ProjectRequest request, Project project) {
		project.setName(request.getName());
	    project.setLocation(request.getLocation());
	    project.setDescription(request.getDescription());
	    project.setBudget(request.getBudget());
	    project.setCurrency(request.getCurrency());
	    project.setStartDate(request.getStartDate());
	    project.setEndDate(request.getEndDate());
	    project.setManager(request.getManager());
	    project.setStatus(request.getStatus());
	    project.setContactPerson(request.getContactPerson());
	    project.setContactPhone(request.getContactPhone());
	    project.setContactEmail(request.getContactEmail());
	}
}
