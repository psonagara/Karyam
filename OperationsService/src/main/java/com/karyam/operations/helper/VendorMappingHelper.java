package com.karyam.operations.helper;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.VendorDTO;
import com.karyam.event.dto.VendorPaymetDTO;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.VendorPaymentRequest;
import com.karyam.operations.dto.request.VendorRequest;
import com.karyam.operations.dto.response.VendorResponse;
import com.karyam.operations.entity.Vendor;
import com.karyam.operations.entity.VendorPayment;
import com.karyam.operations.util.CommonUtil;

public interface VendorMappingHelper {

	public static Vendor toVendor(VendorRequest request) {
        return Vendor.builder()
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .category(request.getCategory())
                .paymentTerms(request.getPaymentTerms())
                .address(request.getAddress())
                .dueAmount(request.getDueAmount())
                .status(request.getStatus())
                .build();
    }
	
	public static VendorResponse toVendorResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .contactPerson(vendor.getContactPerson())
                .phone(vendor.getPhone())
                .email(vendor.getEmail())
                .category(vendor.getCategory())
                .paymentTerms(vendor.getPaymentTerms())
                .address(vendor.getAddress())
                .dueAmount(vendor.getDueAmount())
                .status(vendor.getStatus())
                .createdAt(vendor.getCreatedAt())
                .updatedAt(vendor.getUpdatedAt())
                .build();
    }
	
	public static void updateVendor(VendorRequest request, Vendor vendor) {
        vendor.setName(request.getName());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setPhone(request.getPhone());
        vendor.setEmail(request.getEmail());
        vendor.setCategory(request.getCategory());
        vendor.setPaymentTerms(request.getPaymentTerms());
        vendor.setAddress(request.getAddress());
        vendor.setDueAmount(request.getDueAmount());
        vendor.setStatus(request.getStatus());
    }
	
	public static VendorDTO toVendorDTO(Vendor vendor) {
	    return VendorDTO.builder()
	            .id(vendor.getId())
	            .name(vendor.getName())
	            .contactPerson(vendor.getContactPerson())
	            .phone(vendor.getPhone())
	            .email(vendor.getEmail())
	            .category(vendor.getCategory())
	            .paymentTerms(vendor.getPaymentTerms())
	            .address(vendor.getAddress())
	            .dueAmount(vendor.getDueAmount())
	            .status(vendor.getStatus())
	            .build();
	}
	
	public static KafkaEvent<?> createVendorEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Vendor", oldValue, newValue);
	}

	public static KafkaEvent<?> createVendorPaymentEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "VendorPayment", oldValue, newValue);
	}
	
	public static VendorPayment toVendorPayment(VendorPaymentRequest request) {
		return VendorPayment.builder()
				.amount(request.getAmount())
				.paymentDate(request.getPaymentDate())
				.paymentMethod(request.getPaymentMethod())
				.referenceNumber(request.getReferenceNumber())
				.remarks(request.getRemarks())
				.build();
	}
	
	public static VendorPaymetDTO toVendorPaymetDTO(VendorPayment payment) {
		return VendorPaymetDTO.builder()
				.id(payment.getId())
				.paymentId(payment.getPaymentId())
				.vendorId(payment.getVendor().getId())
				.amount(payment.getAmount())
				.paymentDate(payment.getPaymentDate())
				.paymentMethod(payment.getPaymentMethod())
				.referenceNumber(payment.getReferenceNumber())
				.remarks(payment.getRemarks())
				.build();
	}
}
