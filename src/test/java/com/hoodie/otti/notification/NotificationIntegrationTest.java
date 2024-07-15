package com.hoodie.otti.notification;

import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.repository.notification.NotificationRepository;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NotificationIntegrationTest는 알림 기능의 통합 테스트를 수행하는 클래스입니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * 알림 생성 API의 통합 테스트입니다.
     */
    @Test
    void testCreateNotification() throws Exception {
        // Given
        Notification notification = new Notification("Integration Test Notification");

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Integration Test Notification\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        Notification savedNotification = notificationRepository.findAll().get(0);
        assertEquals("Integration Test Notification", savedNotification.getMessage());
    }

    /**
     * 알림을 읽음 처리하는 API의 통합 테스트입니다.
     */
    @Test
    void testMarkNotificationAsRead() throws Exception {
        // Given
        Notification notification = new Notification("Mark as read test");
        notificationRepository.save(notification);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/notification/" + notification.getUserId() + "/mark-as-read"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        Notification updatedNotification = notificationRepository.findById(notification.getUserId()).orElse(null);
        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isRead());
    }

}
