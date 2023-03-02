package com.example.kltn;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "ECOMMERCE API", version = "1.0", description = "API for KLTN Project"))
public class KltnApplication {

	public static void main(String[] args) {
		SpringApplication.run(KltnApplication.class, args);
	}

}
