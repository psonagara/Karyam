package com.karyam.operations.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.karyam.operations.enu.ActivationStatus;
import com.karyam.operations.enu.LaborType;
import com.karyam.operations.enu.SkillLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaborRequest {

	private String name;
	private String phone;
	private String address;
	private LaborType laborType;
	private BigDecimal dailyWage;
	private Long assignedProjectId;
	private LocalDate joiningDate;
	private ActivationStatus status;
	private BigDecimal experience;
	private SkillLevel skillLevel;
	private String skills;
	private String emergencyContactName;
	private String emergencyContactPhone;
	private String emergencyContactRelation;
	private String bankName;
	private String accountNumber;
	private String ifscCode;
	private String accountHolder;	
}
