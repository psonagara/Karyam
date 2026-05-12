package com.karyam.operations.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IMappingConstants;
import com.karyam.operations.dto.request.ProjectRequest;
import com.karyam.operations.dto.response.ProjectListResponse;
import com.karyam.operations.dto.response.ProjectResponse;
import com.karyam.operations.enu.ProjectStatus;
import com.karyam.operations.service.IProjectService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(IMappingConstants.PROJECT_API)
public class ProjectRestController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);
	
	@Autowired
	private IProjectService projectService;

	@PostMapping
	public ResponseEntity<?> createProject(@RequestBody ProjectRequest request, HttpServletRequest servletRequest) {
		LOG.debug("Enter in ProjectRestController.createProject, " + request);
		String message = projectService.createProject(request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> getAllProject(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.STATUS, required = false) ProjectStatus status,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.STATUS, status);
		
		LOG.debug("Enter in ProjectRestController.getAllProject, " + requestMap);
		ProjectListResponse response = projectService.getAllProject(requestMap, pageable);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getProject(@PathVariable(name = ICommonConstants.ID) Long id) {
		LOG.debug("Enter in ProjectRestController.getProject, " + id);
		ProjectResponse project = projectService.getProjectById(id);
		return CommonUtil.prepareResponseContent(project, HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateProject(
			@PathVariable(name = ICommonConstants.ID) Long projectId,
			@RequestBody ProjectRequest request,
			HttpServletRequest servletRequest) {
		
		LOG.debug("Enter in ProjectRestController.updateProject, id:" + projectId + ", request:" + request);
		String message = projectService.updateProject(projectId, request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteProject(@PathVariable(name = ICommonConstants.ID) Long projectId, HttpServletRequest servletRequest) {
		LOG.debug("Enter in ProjectRestController.deleteProject, " + projectId);
		String message = projectService.deleteProjectById(projectId, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseContent(message, HttpStatus.OK);
	}
	
	@GetMapping("name")
	public ResponseEntity<?> getProjectNames() {
		LOG.debug("Enter in ProjectRestController.getProjectNames");
		List<?> response = projectService.getProjectNames();
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
}
