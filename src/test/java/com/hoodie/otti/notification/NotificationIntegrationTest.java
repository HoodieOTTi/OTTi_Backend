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

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    void testCreateNotification() throws Exception {
        Notification notification = new Notification("Integration Test Notification");

        mockMvc.perform(MockMvcRequestBuilders.post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Integration Test Notification\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Notification savedNotification = notificationRepository.findAll().get(0);

        assertEquals("Integration Test Notification", savedNotification.getMessage());
    }

    @Test
    void testMarkNotificationAsRead() throws Exception {
        Notification notification = new Notification("Mark as read test");
        notificationRepository.save(notification);

        mockMvc.perform(MockMvcRequestBuilders.put("/notification/" + notification.getUserId() + "/mark-as-read"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Notification updatedNotification = notificationRepository.findById(notification.getUserId()).orElse(null);

        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isRead());
    }

}
