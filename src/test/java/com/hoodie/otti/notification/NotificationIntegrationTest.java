package com.hoodie.otti.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        notification1 = new Notification("Notification 1");
        notification1.setUserId(1L);
        notification2 = new Notification("Notification 2");
        notification2.setUserId(2L);
    }

    @Test
    void testGetAllNotifications() throws Exception {
        List<Notification> notifications = Arrays.asList(notification1, notification2);
        when(notificationService.getAllNotifications()).thenReturn(notifications);

        mockMvc.perform(get("/notification"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(notification1.getUserId()))
                .andExpect(jsonPath("$[0].message").value(notification1.getMessage()))
                .andExpect(jsonPath("$[1].id").value(notification2.getUserId()))
                .andExpect(jsonPath("$[1].message").value(notification2.getMessage()));
    }

    @Test
    void testCreateNotification() throws Exception {
        when(notificationService.saveOrUpdateNotification(any(Notification.class))).thenReturn(notification1);

        mockMvc.perform(post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notification1.getUserId()))
                .andExpect(jsonPath("$.message").value(notification1.getMessage()));
    }

    @Test
    void testGetNotificationById() throws Exception {
        when(notificationService.getNotificationById(eq(1L))).thenReturn(notification1);

        mockMvc.perform(get("/notification/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notification1.getUserId()))
                .andExpect(jsonPath("$.message").value(notification1.getMessage()));
    }

    @Test
    void testMarkNotificationAsRead() throws Exception {
        when(notificationService.markNotificationAsRead(eq(1L))).thenReturn(notification1);

        mockMvc.perform(put("/notification/{id}/mark-as-read", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notification1.getUserId()))
                .andExpect(jsonPath("$.message").value(notification1.getMessage()))
                .andExpect(jsonPath("$.isRead").value(true));
    }

    @Test
    void testGetUnreadNotifications() throws Exception {
        List<Notification> unreadNotifications = Arrays.asList(notification1, notification2);
        when(notificationService.getUnreadNotifications(eq(1L))).thenReturn(unreadNotifications);

        mockMvc.perform(get("/notification/unread/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(notification1.getUserId()))
                .andExpect(jsonPath("$[0].message").value(notification1.getMessage()))
                .andExpect(jsonPath("$[1].id").value(notification2.getUserId()))
                .andExpect(jsonPath("$[1].message").value(notification2.getMessage()));
    }

    @Test
    void testCountNotificationsByUserId() throws Exception {
        when(notificationService.countNotificationsByUserId(eq(1L))).thenReturn(2L);

        mockMvc.perform(get("/notification/user/{id}/count", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(2));
    }
}
