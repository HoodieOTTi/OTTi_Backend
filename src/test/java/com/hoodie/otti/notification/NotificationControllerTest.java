package com.hoodie.otti.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hoodie.otti.controller.notification.NotificationController;
import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void testGetAllNotifications() throws Exception {
        Notification notification1 = new Notification("Test message 1");
        Notification notification2 = new Notification("Test message 2");
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationService.getAllNotifications()).thenReturn(notifications);

        mockMvc.perform(get("/notification"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].message", is("Test message 1")))
                .andExpect(jsonPath("$[1].message", is("Test message 2")));
    }

    @Test
    void testCreateNotification() throws Exception {
        Notification notification = new Notification("New message");

        when(notificationService.saveOrUpdateNotification(any(Notification.class)))
                .thenReturn(notification);

        mockMvc.perform(post("/notification")
                        .content(objectMapper.writeValueAsString(notification))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // 상태 코드를 201로 변경
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("New message")));
    }



    @Test
    void testGetNotificationById() throws Exception {
        Long id = 1L;
        Notification notification = new Notification(id, "Test message", false, LocalDateTime.now());

        when(notificationService.getNotificationById(id)).thenReturn(notification);

        mockMvc.perform(get("/notification/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Test message")));
    }

    @Test
    void testMarkNotificationAsRead() throws Exception {
        Long notificationId = 1L;
        Notification notification = new Notification();
        notification.setUserId(notificationId);
        notification.setRead(true);

        when(notificationService.markNotificationAsRead(notificationId)).thenReturn(notification);

        mockMvc.perform(post("/notification/{id}/mark-as-read", notificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notificationId.intValue())))
                .andExpect(jsonPath("$.isRead", is(true)));
    }



    @Test
    void testGetUnreadNotifications() throws Exception {
        Long userId = 1L;
        Notification notification1 = new Notification(userId, "Unread message 1", false, LocalDateTime.now());
        Notification notification2 = new Notification(userId, "Unread message 2", false, LocalDateTime.now());
        List<Notification> unreadNotifications = Arrays.asList(notification1, notification2);

        when(notificationService.getUnreadNotifications(userId)).thenReturn(unreadNotifications);

        mockMvc.perform(get("/notification/unread/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].message", is("Unread message 1")))
                .andExpect(jsonPath("$[1].message", is("Unread message 2")));
    }

    @Test
    void testCountNotificationsByUserId() throws Exception {
        Long userId = 1L;
        long count = 5L;

        when(notificationService.countNotificationsByUserId(userId)).thenReturn(count);

        mockMvc.perform(get("/notification/user/{id}/count", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is((int) count)));
    }
}
