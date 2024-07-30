package com.hoodie.otti;

import com.hoodie.otti.repository.notification.NotificationRepository;
import com.hoodie.otti.service.notification.NotificationService;
import com.hoodie.otti.service.user.KakaoOAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class HoodieOttiApplicationTests {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private KakaoOAuthService kakaoOAuthService;

	@Autowired
	private NotificationRepository notificationRepository;

	@Test
	void contextLoads() {
		assertThat(notificationService).isNotNull();
		assertThat(kakaoOAuthService).isNotNull();
		assertNotNull(notificationRepository);
	}
}
