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
import com.karyam.event.dto.VendorDTO;
import com.karyam.event.dto.VendorPaymetDTO;
import com.karyam.operations.constant.ICommonConstants;
import com.karyam.operations.constant.IExceptionConstants;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.VendorPaymentRequest;
import com.karyam.operations.dto.request.VendorRequest;
import com.karyam.operations.dto.response.VendorListResponse;
import com.karyam.operations.dto.response.VendorResponse;
import com.karyam.operations.entity.User;
import com.karyam.operations.entity.Vendor;
import com.karyam.operations.entity.VendorPayment;
import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.VendorCategory;
import com.karyam.operations.exception.BadRequestException;
import com.karyam.operations.exception.ConflictException;
import com.karyam.operations.exception.InternalServerException;
import com.karyam.operations.exception.ResourceNotFoundException;
import com.karyam.operations.helper.VendorMappingHelper;
import com.karyam.operations.repo.UserRepository;
import com.karyam.operations.repo.VendorPaymentRepository;
import com.karyam.operations.repo.VendorRepository;
import com.karyam.operations.service.IVendorService;
import com.karyam.operations.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VendorServiceImpl implements IVendorService {
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Autowired
	private VendorPaymentRepository vendorPaymentRepository;

	@Override
	public String createVendor(VendorRequest request, RequestMetadata data) {
		boolean exists = vendorRepository.existsByPhoneOrEmail(request.getPhone(), request.getEmail());
		if (exists)
			throw new ConflictException(IExceptionConstants.VENDOR_EXISTS);

		User user = userRepository.findById(JwtUtil.getUserId()).get();
		Vendor vendor = VendorMappingHelper.toVendor(request);
		vendor.setCreatedBy(user);
		try {
			Vendor savedVendor = vendorRepository.save(vendor);
			log.info("Vendor Created with id {}", savedVendor.getId());
			
			// publish event to kafka
			VendorDTO newValue = VendorMappingHelper.toVendorDTO(savedVendor);
			KafkaEvent<?> vendorEvent = VendorMappingHelper.createVendorEvent(data, "vendor.created", null, newValue);
			kafkaProducer.publishVendorEvent(vendorEvent);
		} catch (Exception exception) {
			log.error("Vendor Creation failed, Vendor name: {}", request.getName(), exception);
			throw new InternalServerException(IExceptionConstants.VENDOR_CREATION_FAIL);
		}
		return IResponseConstants.VENDOR_CREATION_SUCESS;
	}

	@Override
	public VendorListResponse filterVendor(Map<String, Object> requestMap, Pageable pageable) {

		Page<Vendor> pages = vendorRepository.filterVendors((VendorCategory) requestMap.get(ICommonConstants.CATEGORY),
				(String) requestMap.get(ICommonConstants.SEARCH), 
				(ActivationStatus) requestMap.get(ICommonConstants.STATUS),
				pageable);
		
		List<Vendor> vendorList = pages.getContent();
		List<VendorResponse> vendorResponseList = vendorList.stream()
				.map(VendorMappingHelper::toVendorResponse)
				.collect(Collectors.toList());
		VendorListResponse response = new VendorListResponse();
		response.setVendors(vendorResponseList);
		response.setTotalPages(pages.getTotalPages());
		response.setNumber(pages.getNumber());
		return response;
	}

	@Override
	public VendorResponse getVendorById(Long vendorId) {
		Optional<Vendor> optional = vendorRepository.findById(vendorId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.VENDOR_NOT_FOUND);
		
		return VendorMappingHelper.toVendorResponse(optional.get());
	}

	@Override
	public String updateVendorDetail(Long vendorId, VendorRequest request, RequestMetadata data) {
		Optional<Vendor> optional = vendorRepository.findById(vendorId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.VENDOR_NOT_FOUND);
		
		Vendor vendor = optional.get();
		VendorDTO oldValue = VendorMappingHelper.toVendorDTO(vendor);
		VendorMappingHelper.updateVendor(request, vendor);
		try {
			Vendor updatedVendor = vendorRepository.save(vendor);
			log.info("Vendor Updated with id {}", updatedVendor.getId());
			
			// publish event to kafka
			VendorDTO updatedValue = VendorMappingHelper.toVendorDTO(updatedVendor);
			KafkaEvent<?> laborEvent = VendorMappingHelper.createVendorEvent(data, "vendor.updated", oldValue, updatedValue);
			kafkaProducer.publishVendorEvent(laborEvent);
		} catch (Exception exception) {
			log.error("Vendor Updation failed, Vendor id: {}", vendor.getId(), exception);
			throw new InternalServerException(IExceptionConstants.VENDOR_UPDATE_FAIL);
		}
		return IResponseConstants.VENDOR_UPDATE_SUCESS;
	}

	@Override
	public String deleteVendorById(Long vendorId, RequestMetadata data) {
		Optional<Vendor> optional = vendorRepository.findById(vendorId);
		if (optional.isEmpty())
			throw new ResourceNotFoundException(IExceptionConstants.VENDOR_NOT_FOUND);
		
		try {
			VendorDTO oldValue = VendorMappingHelper.toVendorDTO(optional.get());
			vendorRepository.deleteById(vendorId);
			
			// publish event to kafka
			KafkaEvent<?> vendorEvent = VendorMappingHelper.createVendorEvent(data, "vendor.deleted", oldValue, null);
			kafkaProducer.publishVendorEvent(vendorEvent);
		} catch (Exception exception) {
			log.error("Vendor Deletion failed, Vendor id: {}", vendorId, exception);
			throw new InternalServerException(IExceptionConstants.VENDOR_UPDATE_FAIL);
		}
		return IResponseConstants.VENDOR_DELETE_SUCESS;
	}

	@Override
	public Map<String, Object> getVendorStats() {
		Map<String, Object> response = new HashMap<>();
		response.put("totalVendors", vendorRepository.count());
		response.put("activeVendors", vendorRepository.countByStatus(ActivationStatus.ACTIVE));
		response.put("totalDueAmount", vendorRepository.findTotalDueAmount());
		return response;
	}
	
	@Override
	public String recordPayment(Long vendorId, VendorPaymentRequest request, RequestMetadata data) {
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException(IExceptionConstants.VENDOR_NOT_FOUND));
		
		if (request.getAmount().compareTo(vendor.getDueAmount()) > 0) {
			throw new BadRequestException(IExceptionConstants.PAYMENT_EXCEED_DUE_AMOUNT);
		}
		User user = userRepository.findById(JwtUtil.getUserId()).get();
		VendorPayment vendorPayment = VendorMappingHelper.toVendorPayment(request);
		vendorPayment.setVendor(vendor);
		vendorPayment.setPaidBy(user);
		try {
			VendorPayment savedPayment = vendorPaymentRepository.save(vendorPayment);
			
			VendorPaymetDTO newValue = VendorMappingHelper.toVendorPaymetDTO(savedPayment);
			KafkaEvent<?> vendorPaymentEvent = VendorMappingHelper.createVendorPaymentEvent(data, "vendor.payment.recorded", null, newValue);
			kafkaProducer.publishVendorEvent(vendorPaymentEvent);
		}  catch (Exception exception) {
			log.error("Payment Record Creation failed, Vendor Id: {}, name: {}", vendor.getId(), vendor.getName(), exception);
			throw new InternalServerException(IExceptionConstants.PAYMENT_CREATION_FAIL);
		}
		return IResponseConstants.PAYMENT_SUCESS;
	}
	
	@Override
	public List<?> getVendorNames() {
		 return vendorRepository.findAllVendorBy();
	}
}
