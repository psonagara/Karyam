package com.karyam.audit.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

	@GetMapping
	public String health() { 
		return "AuditService is up and running";
	}
}
