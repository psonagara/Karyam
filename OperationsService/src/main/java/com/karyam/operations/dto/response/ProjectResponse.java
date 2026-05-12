package com.karyam.operations.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.karyam.operations.enu.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
	
	private Long id;
	private String projectId;
	private String name;
	private String location;
	private String description;
	private BigDecimal budget;
	private String currency;
	private LocalDate startDate;
	private LocalDate endDate;
	private String manager;
	private ProjectStatus status;
	private String contactPerson;
	private String contactPhone;
	private String contactEmail;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
}
