package com.hoodie.otti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.hoodie.otti.repository.profile"})
public class HoodieOttiApplication {
	public static void main(String[] args) {
		SpringApplication.run(HoodieOttiApplication.class, args);
	}
}

