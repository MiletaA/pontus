package com.pontus.vessel;

import org.springframework.boot.SpringApplication;

public class TestVesselServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(VesselServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
