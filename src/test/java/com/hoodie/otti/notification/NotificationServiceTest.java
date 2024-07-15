package com.hoodie.otti.notification;

import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.exception.notification.NotificationNotFoundException;
import com.hoodie.otti.repository.notification.NotificationRepository;
import com.hoodie.otti.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllNotifications() {
        // 테스트용 데이터 생성
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Test notification"));

        // Mock 객체 설정
        when(notificationRepository.findAll()).thenReturn(notifications);

        // 서비스 메서드 호출
        List<Notification> result = notificationService.getAllNotifications();

        // 검증
        assertEquals(1, result.size());
        assertEquals("Test notification", result.get(0).getMessage());
    }

    @Test
    void testGetNotificationById() {
        // 테스트용 데이터 생성
        Notification notification = new Notification("Test notification");
        notification.setUserId(1L);

        // Mock 객체 설정
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        // 서비스 메서드 호출
        Notification result = notificationService.getNotificationById(1L);

        // 검증
        assertNotNull(result);
        assertEquals("Test notification", result.getMessage());
    }

    @Test
    void testGetNotificationById_NotFound() {
        // Mock 객체 설정 (빈 Optional 반환)
        when(notificationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // 예외 발생 여부 확인
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotificationById(1L));
    }

    @Test
    void testSaveOrUpdateNotification() {
        // 테스트용 데이터 생성
        Notification notification = new Notification("New notification");

        // Mock 객체 설정
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // 서비스 메서드 호출
        Notification result = notificationService.saveOrUpdateNotification(notification);

        // 검증
        assertNotNull(result);
        assertEquals("New notification", result.getMessage());
    }

    @Test
    void testMarkNotificationAsRead() {
        // 테스트용 데이터 생성
        Notification notification = new Notification("Test notification");
        notification.setRead(false);

        // Mock 객체 설정
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // 서비스 메서드 호출
        Notification result = notificationService.markNotificationAsRead(1L);

        // 검증
        assertTrue(result.isRead());
    }

    @Test
    void testGetUnreadNotifications() {
        // 테스트용 데이터 생성
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Unread notification"));

        // Mock 객체 설정
        when(notificationRepository.findByIsReadFalseAndUserId(1L)).thenReturn(notifications);

        // 서비스 메서드 호출
        List<Notification> result = notificationService.getUnreadNotifications(1L);

        // 검증
        assertEquals(1, result.size());
        assertEquals("Unread notification", result.get(0).getMessage());
    }

    @Test
    void testCountNotificationsByUserId() {
        // Mock 객체 설정
        when(notificationRepository.countByUserId(1L)).thenReturn(5L);

        // 서비스 메서드 호출
        long result = notificationService.countNotificationsByUserId(1L);

        // 검증
        assertEquals(5, result);
    }
}
