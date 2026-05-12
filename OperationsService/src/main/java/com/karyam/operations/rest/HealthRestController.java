package com.karyam.operations.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

	@GetMapping
	public String healthCheck() {
		return "Operations Service is running";
	}
}
