package com.karyam.operations.helper;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.event.dto.LaborDTO;
import com.karyam.operations.dto.RequestMetadata;
import com.karyam.operations.dto.request.LaborRequest;
import com.karyam.operations.dto.response.LaborResponse;
import com.karyam.operations.entity.Labor;
import com.karyam.operations.util.CommonUtil;

public interface LaborMappingHelper {

	public static Labor toLabor(LaborRequest request) {
		return Labor.builder()
				.name(request.getName())
				.phone(request.getPhone())
				.address(request.getAddress())
				.laborType(request.getLaborType())
				.dailyWage(request.getDailyWage())
				.joiningDate(request.getJoiningDate())
				.status(request.getStatus())
				.experience(request.getExperience())
				.skillLevel(request.getSkillLevel())
				.skills(request.getSkills())
				.emergencyContactName(request.getEmergencyContactName())
				.emergencyContactPhone(request.getEmergencyContactPhone())
				.emergencyContactRelation(request.getEmergencyContactRelation())
				.bankName(request.getBankName())
				.accountNumber(request.getAccountNumber())
				.ifscCode(request.getIfscCode())
				.accountHolder(request.getAccountHolder())
				.build();
	}

	public static LaborDTO toLaborDTO(Labor labor) {
		return LaborDTO.builder()
				.laborId(labor.getId())
				.laborDisplayId(labor.getLaborId())
				.name(labor.getName())
				.phone(labor.getPhone())
				.address(labor.getAddress())
				.laborType(labor.getLaborType())
				.dailyWage(labor.getDailyWage())
				.assignedProjectId(labor.getAssignedProject() != null ? labor.getAssignedProject().getProjectId() : "")
				.joiningDate(labor.getJoiningDate())
				.status(labor.getStatus())
				.experience(labor.getExperience())
				.skillLevel(labor.getSkillLevel())
				.skills(labor.getSkills())
				.emergencyContactName(labor.getEmergencyContactName())
				.emergencyContactPhone(labor.getEmergencyContactPhone())
				.emergencyContactRelation(labor.getEmergencyContactRelation())
				.bankName(labor.getBankName())
				.accountNumber(labor.getAccountNumber())
				.ifscCode(labor.getIfscCode())
				.accountHolder(labor.getAccountHolder())
				.build();
	}
	
	public static KafkaEvent<?> createLaborEvent(RequestMetadata data, String eventType, Object oldValue, Object newValue) {
		return CommonUtil.createKafkaEvent(data, eventType, "Labor", oldValue, newValue);
	}
	
	public static LaborResponse toLaborResponse(Labor labor) {
		return LaborResponse.builder()
				.id(labor.getId())
				.laborId(labor.getLaborId())
				.name(labor.getName())
				.phone(labor.getPhone())
				.address(labor.getAddress())
				.laborType(labor.getLaborType())
				.dailyWage(labor.getDailyWage())
				.assignedProjectName(labor.getAssignedProject() != null ? labor.getAssignedProject().getName() : null)
				.assignedProjectId(labor.getAssignedProject() != null ? labor.getAssignedProject().getId() : null)
				.joiningDate(labor.getJoiningDate())
				.status(labor.getStatus())
				.experience(labor.getExperience())
				.skillLevel(labor.getSkillLevel())
				.skills(labor.getSkills())
				.emergencyContactName(labor.getEmergencyContactName())
				.emergencyContactPhone(labor.getEmergencyContactPhone())
				.emergencyContactRelation(labor.getEmergencyContactRelation())
				.bankName(labor.getBankName())
				.accountNumber(labor.getAccountNumber())
				.ifscCode(labor.getIfscCode())
				.accountHolder(labor.getAccountHolder())
				.createdAt(labor.getCreatedAt())
				.updatedAt(labor.getUpdatedAt())
				.build();
	}

	public static void updateLabor(LaborRequest request, Labor labor) {
	    labor.setName(request.getName());
	    labor.setPhone(request.getPhone());
	    labor.setAddress(request.getAddress());
	    labor.setLaborType(request.getLaborType());
	    labor.setDailyWage(request.getDailyWage());
	    labor.setJoiningDate(request.getJoiningDate());
	    labor.setStatus(request.getStatus());
	    labor.setExperience(request.getExperience());
	    labor.setSkillLevel(request.getSkillLevel());
	    labor.setSkills(request.getSkills());
	    labor.setEmergencyContactName(request.getEmergencyContactName());
	    labor.setEmergencyContactPhone(request.getEmergencyContactPhone());
	    labor.setEmergencyContactRelation(request.getEmergencyContactRelation());
	    labor.setBankName(request.getBankName());
	    labor.setAccountNumber(request.getAccountNumber());
	    labor.setIfscCode(request.getIfscCode());
	    labor.setAccountHolder(request.getAccountHolder());
	}
}
