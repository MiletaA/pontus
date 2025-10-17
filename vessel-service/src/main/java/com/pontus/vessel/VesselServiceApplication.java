package com.pontus.vessel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VesselServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VesselServiceApplication.class, args);
	}

}
