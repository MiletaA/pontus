package com.pontus.cargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import com.pontus.cargo.config.SecurityConfig;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Import(SecurityConfig.class) // Explicitly import SecurityConfig to ensure Spring Security is loaded
public class CargoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CargoServiceApplication.class, args);
	}

}
