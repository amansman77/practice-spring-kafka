package com.ho.sample.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaComsumerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KafkaComsumerApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

}
