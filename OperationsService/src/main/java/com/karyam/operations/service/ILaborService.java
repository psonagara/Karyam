package com.karyam.operations.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.LaborRequest;
import com.karyam.operations.dto.response.LaborListResponse;
import com.karyam.operations.dto.response.LaborResponse;

public interface ILaborService {

	String createLabor(LaborRequest request, RequestMetadata data);
	LaborListResponse filterLabor(Map<String, Object> requestMap, Pageable pageable);
	LaborResponse getLaborById(Long laborId);
	String updateLaborDetail(Long laborId, LaborRequest request, RequestMetadata data);
	String deleteLaborById(Long laborId, RequestMetadata data);
	Map<String, Object> getLaborStats();
}
