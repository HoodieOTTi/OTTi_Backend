package com.hoodie.otti.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.controller.notification.NotificationController;
import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void testGetAllNotifications() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Test notification"));

        when(notificationService.getAllNotifications()).thenReturn(notifications);

        mockMvc.perform(get("/notification")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].message").value("Test notification"));
    }

    @Test
    public void testCreateNotification() throws Exception {
        LocalDateTime createdAt = LocalDateTime.now();
        Notification notification = new Notification();
        notification.setUserId(1L);
        notification.setMessage("Test Notification");
        notification.setCreatedAt(createdAt);

        when(notificationService.saveOrUpdateNotification(any(Notification.class))).thenReturn(notification);

        mockMvc.perform(MockMvcRequestBuilders.post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("Test Notification"))
                .andExpect(jsonPath("$.createdAt").value(createdAt.toString()));
    }

    @Test
    void testGetNotificationById() throws Exception {
        Notification notification = new Notification("Test notification");
        notification.setUserId(1L);

        when(notificationService.getNotificationById(1L)).thenReturn(notification);

        mockMvc.perform(get("/notification/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void testMarkNotificationAsRead() throws Exception {
        Notification notification = new Notification("Test notification");
        notification.setUserId(1L);

        when(notificationService.markNotificationAsRead(1L)).thenReturn(notification);

        mockMvc.perform(put("/notification/1/mark-as-read")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"))
                .andExpect(jsonPath("$.read").value(true));
    }

    @Test
    void testGetUnreadNotifications() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Unread notification"));

        when(notificationService.getUnreadNotifications(1L)).thenReturn(notifications);

        mockMvc.perform(get("/notification/unread/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].message").value("Unread notification"));
    }

    @Test
    void testCountNotificationsByUserId() throws Exception {
        when(notificationService.countNotificationsByUserId(1L)).thenReturn(5L);

        mockMvc.perform(get("/notification/user/1/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
