package com.hoodie.otti;

import com.hoodie.otti.repository.delete.DeleteUserRepository;
import com.hoodie.otti.service.delete.DeleteUserService;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DeleteUserService.class, DeleteUserRepository.class})
class HoodieOttiApplicationTests {

	@Autowired
	private HoodieOttiApplication application;

	@MockBean
	private NotificationService notificationService; // 외부 서비스 모킹

	@Test
	void contextLoads() {
		// 애플리케이션 객체가 정상적으로 생성되었는지 검증
		assertThat(application).isNotNull();

		// NotificationService가 모킹되었는지 검증
		assertThat(notificationService).isNotNull();
	}

}
