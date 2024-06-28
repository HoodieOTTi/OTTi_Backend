package com.hoodie.otti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HoodieOttiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoodieOttiApplication.class, args);
	}

}
