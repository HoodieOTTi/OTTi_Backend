package com.hoodie.otti.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.controller.notification.NotificationController;
import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() throws Exception {
        // Mock 데이터
        Notification notification1 = new Notification("Notification 1");
        Notification notification2 = new Notification("Notification 2");
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Mock service behavior
        when(notificationService.getAllNotifications()).thenReturn(notifications);

        // MockMvc를 이용한 API 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/notification"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value("Notification 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value("Notification 2"));
    }

    @Test
    void testCreateNotification() throws Exception {
        // Mock 데이터
        Notification notification = new Notification("New Notification");

        // Mock service behavior
        when(notificationService.saveOrUpdateNotification(any(Notification.class))).thenReturn(notification);

        // MockMvc를 이용한 API 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(notification)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("New Notification"));
    }

    @Test
    void testGetNotificationById() throws Exception {
        // Mock 데이터
        Long notificationId = 1L;
        Notification notification = new Notification(notificationId, "Test Notification", false, null);

        // Mock service behavior
        when(notificationService.getNotificationById(notificationId)).thenReturn(notification);

        // MockMvc를 이용한 API 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/{id}", notificationId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Test Notification"));
    }

    @Test
    void testMarkNotificationAsRead() throws Exception {
        // Mock 데이터
        Long notificationId = 1L;
        Notification notification = new Notification(notificationId, "Test Notification", false, null);

        // Mock service behavior
        when(notificationService.markNotificationAsRead(notificationId)).thenReturn(notification);

        // MockMvc를 이용한 API 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/notification/{id}/mark-as-read", notificationId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isRead").value(true));
    }

    @Test
    void testGetUnreadNotifications() throws Exception {
        // Mock 데이터
        Long userId = 1L;
        Notification notification1 = new Notification("Notification 1");
        Notification notification2 = new Notification("Notification 2");
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Mock service behavior
        when(notificationService.getUnreadNotifications(userId)).thenReturn(notifications);

        // MockMvc를 이용한 API 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/unread/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value("Notification 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value("Notification 2"));
    }

    @Test
    void testCountNotificationsByUserId() throws Exception {
        // Mock 데이터
        Long userId = 1L;
        long count = 5;

        // Mock service behavior
        when(notificationService.countNotificationsByUserId(userId)).thenReturn(count);

        // MockMvc를 이용한 API 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/user/{id}/count", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(count)));
    }

}
