package com.hoodie.otti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.hoodie.otti.entity")
@ComponentScan(basePackages = "com.hoodie.otti.service.delete")
@ComponentScan("com.hoodie.otti")
public class HoodieOttiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoodieOttiApplication.class, args);
	}
}
