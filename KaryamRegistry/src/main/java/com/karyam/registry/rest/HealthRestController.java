package com.karyam.registry.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

	@GetMapping
	public ResponseEntity<?> health() {
		return ResponseEntity.ok("KaryamRegistry is up and running");
	}
}
