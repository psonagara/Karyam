package com.karyam.operations.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.LaborDTO;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.LaborRequest;
import com.karyam.operations.dto.response.LaborListResponse;
import com.karyam.operations.dto.response.LaborResponse;
import com.karyam.operations.entity.Labor;
import com.karyam.operations.entity.Project;
import com.karyam.operations.entity.User;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.LaborType;
import com.karyam.operations.exception.ConflictException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.LaborMappingHelper;
import com.karyam.operations.repo.LaborRepository;
import com.karyam.operations.repo.ProjectRepository;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.service.ILaborService;
import com.karyam.operations.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LaborServiceImpl implements ILaborService {
	
	@Autowired
	private LaborRepository laborRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;

	@Override
	public String createLabor(LaborRequest request, RequestMetadata data) {
		boolean exists = laborRepository.existsByPhone(request.getPhone());
		if (exists)
			throw new ConflictException(IExceptionConstants.LABOR_EXISTS);
		
		Long assignedProjectId = request.getAssignedProjectId();
		Labor labor = LaborMappingHelper.toLabor(request);
		if (assignedProjectId != null) {
			Optional<Project> optional = projectRepository.findById(assignedProjectId);
			if (optional.isEmpty())
				throw new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND);
			labor.setAssignedProject(optional.get());
		}
		User user = userRepository.findById(JwtUtil.getUserId()).get();
		labor.setCreatedBy(user);
		
		try {
			Labor savedLabor = laborRepository.save(labor);
			log.info("Labor Created with id {}", savedLabor.getId());
			
			// publish event to kafka
			LaborDTO newValue = LaborMappingHelper.toLaborDTO(savedLabor);
			KafkaEvent<?> laborEvent = LaborMappingHelper.createLaborEvent(data, "labor.created", null, newValue);
			kafkaProducer.publishLaborEvent(laborEvent);
		} catch (Exception exception) {
			log.error("Labor Creation failed, Labor name: {}", request.getName(), exception);
			throw new InternalServerException(IExceptionConstants.LABOR_CREATION_FAIL);
		}
		return IResponseConstants.LABOR_CREATION_SUCESS;
	}

	@Override
	public LaborListResponse filterLabor(Map<String, Object> requestMap, Pageable pageable) {
		
		Long projectId = (Long) requestMap.get(ICommonConstants.PROJECT_ID);
		boolean unAssigned = false;
		if (projectId != null && projectId == -1) {
			unAssigned = true;
			projectId = null;
		}
		Page<Labor> pages = laborRepository.filterLabors((ActivationStatus) requestMap.get(ICommonConstants.STATUS), 
				(String) requestMap.get(ICommonConstants.SEARCH),
				projectId,
				(LaborType) requestMap.get(ICommonConstants.LABOR_TYPE), 
				unAssigned,
				pageable);
		
		List<Labor> laborList = pages.getContent();
		List<LaborResponse> laborResponseList = laborList.stream()
				.map(LaborMappingHelper::toLaborResponse)
				.collect(Collectors.toList());
		LaborListResponse response = new LaborListResponse();
		response.setLabors(laborResponseList);
		response.setTotalPages(pages.getTotalPages());
		response.setNumber(pages.getNumber());
		return response;
	}

	@Override
	public LaborResponse getLaborById(Long laborId) {
		Optional<Labor> optional = laborRepository.findById(laborId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.LABOR_NOT_FOUND);
		
		return LaborMappingHelper.toLaborResponse(optional.get());
	}

	@Override
	public String updateLaborDetail(Long laborId, LaborRequest request, RequestMetadata data) {
		
		Optional<Labor> optional = laborRepository.findById(laborId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.LABOR_NOT_FOUND);
		
		Labor labor = optional.get();
		LaborDTO oldValue = LaborMappingHelper.toLaborDTO(labor);
		LaborMappingHelper.updateLabor(request, labor);
		
		Long assignedProjectId = request.getAssignedProjectId();
		if (assignedProjectId != null) {
			Optional<Project> projectOptional = projectRepository.findById(assignedProjectId);
			if (projectOptional.isEmpty())
				throw new ResourceNotFoundException(IExceptionConstants.PROJECT_NOT_FOUND);
			labor.setAssignedProject(projectOptional.get());
		} else {
			labor.setAssignedProject(null);
		}
		
		try {
			Labor updatedLabor = laborRepository.save(labor);
			log.info("Labor Updated with id {}", updatedLabor.getId());
			
			// publish event to kafka
			LaborDTO updatedValue = LaborMappingHelper.toLaborDTO(updatedLabor);
			KafkaEvent<?> laborEvent = LaborMappingHelper.createLaborEvent(data, "labor.updated", oldValue, updatedValue);
			kafkaProducer.publishLaborEvent(laborEvent);
		} catch (Exception exception) {
			log.error("Labor Updation failed, Labor id: {}", labor.getId(), exception);
			throw new InternalServerException(IExceptionConstants.LABOR_UPDATE_FAIL);
		}
		return IResponseConstants.LABOR_UPDATE_SUCESS;
	}

	@Override
	public String deleteLaborById(Long laborId, RequestMetadata data) {
		Optional<Labor> optional = laborRepository.findById(laborId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.LABOR_NOT_FOUND);
		
		try {
			LaborDTO oldValue = LaborMappingHelper.toLaborDTO(optional.get());
			laborRepository.deleteById(laborId);
			
			// publish event to kafka
			KafkaEvent<?> laborEvent = LaborMappingHelper.createLaborEvent(data, "labor.deleted", oldValue, null);
			kafkaProducer.publishLaborEvent(laborEvent);
		} catch (Exception exception) {
			log.error("Labor Deletion failed, Labor id: {}", laborId, exception);
			throw new InternalServerException(IExceptionConstants.LABOR_UPDATE_FAIL);
		}
		return IResponseConstants.LABOR_DELETE_SUCESS;
	}

	@Override
	public Map<String, Object> getLaborStats() {
		Map<String, Object> response = new HashMap<>();
		response.put("totalWorkers", laborRepository.count());
		response.put("activeWorkers", laborRepository.countByStatus(ActivationStatus.ACTIVE));
		response.put("averageDailyWage", laborRepository.findAverageDailyWage());
		return response;
	}

}
