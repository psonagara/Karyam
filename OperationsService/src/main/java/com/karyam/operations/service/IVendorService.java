package com.karyam.operations.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.VendorPaymentRequest;
import com.karyam.operations.dto.request.VendorRequest;
import com.karyam.operations.dto.response.VendorListResponse;
import com.karyam.operations.dto.response.VendorResponse;

public interface IVendorService {

	String createVendor(VendorRequest request, RequestMetadata data);
	VendorListResponse filterVendor(Map<String, Object> requestMap, Pageable pageable);
	VendorResponse getVendorById(Long vendorId);
	String updateVendorDetail(Long vendorId, VendorRequest request, RequestMetadata data);
	String deleteVendorById(Long vendorId, RequestMetadata data);
	Map<String, Object> getVendorStats();
	String recordPayment(Long vendorId, VendorPaymentRequest request, RequestMetadata data);
	List<?> getVendorNames();
}
