package com.karyam.operations.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.ProjectDTO;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.ProjectRequest;
import com.karyam.operations.dto.response.ProjectListResponse;
import com.karyam.operations.dto.response.ProjectResponse;
import com.karyam.operations.entity.Project;
import com.karyam.operations.entity.User;
import com.karyam.operations.enu.ProjectStatus;
import com.karyam.operations.exception.ConflictException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.ProjectMappingHelper;
import com.karyam.operations.repo.ProjectRepository;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.service.IProjectService;
import com.karyam.operations.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectServiceImpl implements IProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;

	@Override
	public String createProject(ProjectRequest request, RequestMetadata data) {
		boolean exists = projectRepository.existsByName(request.getName());
		if (exists)
			throw new ConflictException(IExceptionConstants.PROJECT_EXISTS);
		
		User user = userRepository.findById(JwtUtil.getUserId()).get();
		Project project = ProjectMappingHelper.toProject(request);
		project.setCreatedBy(user);
		try {
			Project savedProject = projectRepository.save(project);
			log.info("Project Created with id {}", savedProject.getId());
			
			// publish event to kafka
			ProjectDTO newValue = ProjectMappingHelper.toProjectDTO(savedProject);
			KafkaEvent<?> projectEvent = ProjectMappingHelper.createProjectEvent(data, "project.created", null, newValue);
			kafkaProducer.publishProjectEvent(projectEvent);
		} catch (Exception exception) {
			log.error("Project Creation failed, Project name: {}", request.getName(), exception);
			throw new InternalServerException(IExceptionConstants.PROJECT_CREATION_FAIL);
		}
		return IResponseConstants.PROJECT_CREATION_SUCESS;
	}

	@Override
	public ProjectListResponse getAllProject(Map<String, Object> requestMap, Pageable pageable) {
		Page<Project> pages = projectRepository
				.filterProjects((ProjectStatus) requestMap.get(ICommonConstants.STATUS), (String) requestMap.get(ICommonConstants.SEARCH), pageable);
		List<Project> projectList = pages.getContent();
		List<ProjectResponse> projectResponseList = projectList.stream()
				.map(ProjectMappingHelper::toProjectResponse)
				.collect(Collectors.toList());
		
		ProjectListResponse response = new ProjectListResponse();
		response.setProjects(projectResponseList);
		response.setTotalPages(pages.getTotalPages());
		response.setNumber(pages.getNumber());
		return response;
	}

	@Override
	public ProjectResponse getProjectById(Long id) {
		Optional<Project> optional = projectRepository.findById(id);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND);
		
		return ProjectMappingHelper.toProjectResponse(optional.get());
	}

	@Override
	public String updateProject(Long projectId, ProjectRequest request, RequestMetadata data) {
		Optional<Project> optional = projectRepository.findById(projectId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND);
		
		Project project = optional.get();
		ProjectDTO oldValue = ProjectMappingHelper.toProjectDTO(project);
		ProjectMappingHelper.updateProject(request, project);
		try {
			Project updatedProject = projectRepository.save(project);
			log.info("Project Updated with id {}", updatedProject.getId());
			
			// publish event to kafka
			ProjectDTO updatedValue = ProjectMappingHelper.toProjectDTO(updatedProject);
			KafkaEvent<?> projectEvent = ProjectMappingHelper.createProjectEvent(data, "project.updated", oldValue, updatedValue);
			kafkaProducer.publishProjectEvent(projectEvent);
		} catch (Exception exception) {
			log.error("Project Updation failed, Project id: {}", project.getId(), exception);
			throw new InternalServerException(IExceptionConstants.PROJECT_UPDATE_FAIL);
		}
		return IResponseConstants.PROJECT_UPDATE_SUCESS;
	}

	@Override
	public String deleteProjectById(Long projectId, RequestMetadata data) {
		Optional<Project> optional = projectRepository.findById(projectId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND);
		
		try {
			ProjectDTO oldValue = ProjectMappingHelper.toProjectDTO(optional.get());
			projectRepository.deleteById(projectId);
			
			// publish event to kafka
			KafkaEvent<?> projectEvent = ProjectMappingHelper.createProjectEvent(data, "project.deleted", oldValue, null);
			kafkaProducer.publishProjectEvent(projectEvent);
		} catch (Exception exception) {
			log.error("Project Deletion failed, Project id: {}", projectId, exception);
			throw new InternalServerException(IExceptionConstants.PROJECT_UPDATE_FAIL);
		}
		return IResponseConstants.PROJECT_DELETE_SUCESS;
	}

	@Override
	public List<?> getProjectNames() {
		 return projectRepository.findAllProjectBy();
	}
}
