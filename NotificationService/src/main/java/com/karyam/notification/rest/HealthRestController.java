package com.karyam.notification.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

	@GetMapping
	public ResponseEntity<?> health() {
		return ResponseEntity.ok("Notification Service is UP and running");
	}
}
