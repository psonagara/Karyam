package com.karyam.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.karyam.audit.enu.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {

	private Long projectId;
	private String projectDisplayId;
	private String projectName;
	private String location;
	private BigDecimal budget;
	private String currency;
	private LocalDate startDate;
	private LocalDate endDate;
	private ProjectStatus status;
	private String description;
	private String manager;
	private String contactPerson;
	private String contactPhone;
	private String contactEmail;
}
