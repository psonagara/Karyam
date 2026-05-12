package com.karyam.operations.rest;

import java.util.HashMap;
import java.util.List;
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
import com.karyam.operations.dto.request.VendorPaymentRequest;
import com.karyam.operations.dto.request.VendorRequest;
import com.karyam.operations.dto.response.VendorListResponse;
import com.karyam.operations.dto.response.VendorResponse;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.VendorCategory;
import com.karyam.operations.service.IVendorService;
import com.karyam.operations.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(IMappingConstants.VENDOR_API)
@Slf4j
public class VendorRestController {

	@Autowired
	private IVendorService vendorService;

	@PostMapping
	public ResponseEntity<?> registerVendor(@RequestBody VendorRequest request, HttpServletRequest servletRequest) {
		log.debug("Enter in VendorRestController.registerVendor, " + request);
		String message = vendorService.createVendor(request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> getAllVendor(
			@RequestParam(name = ICommonConstants.SEARCH, required = false) String search,
			@RequestParam(name = ICommonConstants.CATEGORY, required = false) VendorCategory category,
			@RequestParam(name = ICommonConstants.STATUS, required = false) ActivationStatus status,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ICommonConstants.SEARCH, search);
		requestMap.put(ICommonConstants.CATEGORY, category);
		requestMap.put(ICommonConstants.STATUS, status);
		
		log.debug("Enter in VendorRestController.getAllVendor, " + requestMap);
		VendorListResponse response = vendorService.filterVendor(requestMap, pageable);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
	
	@GetMapping("stats")
	public ResponseEntity<?> getVendorStats() {
		return ResponseEntity.ok(vendorService.getVendorStats());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getVendor(@PathVariable(name = ICommonConstants.ID) Long vendorId) {
		log.debug("Enter in VendorRestController.getVendor, " + vendorId);
		VendorResponse response = vendorService.getVendorById(vendorId);
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateVendor(
			@PathVariable(name = ICommonConstants.ID) Long vendorId,
			@RequestBody VendorRequest request,
			HttpServletRequest servletRequest) {
		
		log.debug("Enter in VendorRestController.updateVendor, id:" + vendorId + ", request:" + request);
		String message = vendorService.updateVendorDetail(vendorId, request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseMessage(message, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteVendor(@PathVariable(name = ICommonConstants.ID) Long vendorId, HttpServletRequest servletRequest) {
		log.debug("Enter in VendorRestController.deleteVendor, " + vendorId);
		String message = vendorService.deleteVendorById(vendorId, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseContent(message, HttpStatus.OK);
	}
	
	@PostMapping("{id}/payment")
	public ResponseEntity<?> recordPayment(@RequestBody VendorPaymentRequest request, 
			@PathVariable(name = "id") Long vendorId,
			HttpServletRequest servletRequest) {
		log.debug("Enter in VendorRestController.recordPayment, Id:" + vendorId + ", request:" + request);
		String message = vendorService.recordPayment(vendorId, request, CommonUtil.getRequestMetadata(servletRequest));
		return CommonUtil.prepareResponseContent(message, HttpStatus.CREATED);
	}
	
	@GetMapping("name")
	public ResponseEntity<?> getVendorNames() {
		log.debug("Enter in VendorRestController.getVendorNames");
		List<?> response = vendorService.getVendorNames();
		return CommonUtil.prepareResponseContent(response, HttpStatus.OK);
	}
}
