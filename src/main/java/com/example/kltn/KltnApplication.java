package com.example.kltn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "ECOMMERCE API", version = "1.0", description = "API for KLTN Project"))
@EnableMongoAuditing
public class KltnApplication {

	public static void main(String[] args) {
		SpringApplication.run(KltnApplication.class, args);
	}

}
