package com.hoodie.otti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HoodieOttiApplicationTests {

	@Autowired
	private HoodieOttiApplication application;

	@Test
	void contextLoads() {
		assertThat(application).isNotNull();
	}

}
