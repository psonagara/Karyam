package com.karyam.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class KaryamRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaryamRegistryApplication.class, args);
	}

}
