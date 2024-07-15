package com.hoodie.otti.notification;

import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.exception.notification.NotificationNotFoundException;
import com.hoodie.otti.repository.notification.NotificationRepository;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() {
        // Mock 데이터
        Notification notification1 = new Notification("Notification 1");
        Notification notification2 = new Notification("Notification 2");
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Mock repository behavior
        when(notificationRepository.findAll()).thenReturn(notifications);

        // 호출 및 검증
        List<Notification> result = notificationService.getAllNotifications();
        assertEquals(2, result.size());
        assertEquals(notification1, result.get(0));
        assertEquals(notification2, result.get(1));
    }

    @Test
    void testGetNotificationById() {
        // Mock 데이터
        Long notificationId = 1L;
        Notification notification = new Notification(notificationId, "Test Notification", false, null);

        // Mock repository behavior
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // 호출 및 검증
        Notification result = notificationService.getNotificationById(notificationId);
        assertEquals(notificationId, result.getUserId());
        assertEquals("Test Notification", result.getMessage());
        assertFalse(result.isRead());
    }

    @Test
    void testGetNotificationById_NotFound() {
        // Mock repository behavior
        when(notificationRepository.findById(any())).thenReturn(Optional.empty());

        // 호출 시 예외 발생 여부 검증
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotificationById(999L));
    }

    @Test
    void testSaveOrUpdateNotification() {
        // Mock 데이터
        Notification notification = new Notification("New Notification");

        // Mock repository behavior
        when(notificationRepository.save(notification)).thenReturn(notification);

        // 호출 및 검증
        Notification result = notificationService.saveOrUpdateNotification(notification);
        assertNotNull(result);
        assertEquals("New Notification", result.getMessage());
    }

    @Test
    void testDeleteNotification() {
        // Mock 데이터
        Long notificationId = 1L;

        // 호출
        notificationService.deleteNotification(notificationId);

        // 삭제가 호출되었는지 검증
        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

}