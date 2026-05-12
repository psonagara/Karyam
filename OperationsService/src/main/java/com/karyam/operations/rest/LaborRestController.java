package com.karyam.operations.rest;

import java.util.HashMap;
import java.util.Map;

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
import com.karyam.operations.dto.request.LaborRequest;
import com.karyam.operations.dto.response.LaborListResponse;
import com.karyam.operations.dto.response.LaborResponse;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.LaborType;
import com.karyam.operations.service.ILaborService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.LABOR_API)
@Slf4j
public class LaborRestController {
	
	@Autowired
	private ILaborService laborService;

	@PostMapping
	public ResponseEntity<?> registerLabor(@RequestBody LaborRequest request, HttpServletRequest servletRequest) {
		log.debug("Enter in LaborRestController.registerLabor, " + request);
		String message = laborService.createLabor(request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> getAllLabor(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.LABOR_TYPE, required = false) LaborType laborType,
			@RequestParam(name = ICommonConstants.STATUS, required = false) ActivationStatus status,
			@RequestParam(name = ICommonConstants.PROJECT_ID, required = false) Long projectId,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.LABOR_TYPE, laborType);
		requestMap.put(ICommonConstants.STATUS, status);
		requestMap.put(ICommonConstants.PROJECT_ID, projectId);
		
		log.debug("Enter in LaborRestController.getAllLabor, " + requestMap);
		LaborListResponse response = laborService.filterLabor(requestMap, pageable);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("stats")
	public ResponseEntity<?> getLaborStats() {
		return ResponseEntity.ok(laborService.getLaborStats());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getLabor(@PathVariable(name = ICommonConstants.ID) Long laborId) {
		log.debug("Enter in LaborRestController.getLabor, " + laborId);
		LaborResponse response = laborService.getLaborById(laborId);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateLabor(
			@PathVariable(name = ICommonConstants.ID) Long laborId,
			@RequestBody LaborRequest request,
			HttpServletRequest servletRequest) {
		
		log.debug("Enter in LaborRestController.updateLabor, id:" + laborId + ", request:" + request);
		String message = laborService.updateLaborDetail(laborId, request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteLabor(@PathVariable(name = ICommonConstants.ID) Long laborId, HttpServletRequest servletRequest) {
		log.debug("Enter in LaborRestController.deleteLabor, " + laborId);
		String message = laborService.deleteLaborById(laborId, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseContent(message, HttpStatus.OK);
	}
}
