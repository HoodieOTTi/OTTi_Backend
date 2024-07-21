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
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * NotificationServiceTest는 알림 관련 서비스 클래스의 단위 테스트를 수행합니다.
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    /**
     * 각 테스트 메서드 실행 전 초기화 작업을 수행할 수 있도록 설정합니다.
     */
    @BeforeEach
    void setUp() {
    }

    /**
     * 모든 알림을 조회하는 테스트입니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testGetAllNotifications() {
        // Given
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Test notification"));

        // Mock 설정
        when(notificationRepository.findAll()).thenReturn(notifications);

        // When
        List<Notification> result = notificationService.getAllNotifications();

        // Then
        assertEquals(1, result.size());
        assertEquals("Test notification", result.get(0).getMessage());
    }

    /**
     * 특정 ID로 알림을 조회하는 테스트입니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testGetNotificationById() {
        // Given
        Notification notification = new Notification("Test notification");
        notification.setUserId(1L);

        // Mock 설정
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        // When
        Notification result = notificationService.getNotificationById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test notification", result.getMessage());
    }

    /**
     * 존재하지 않는 ID로 알림을 조회할 때 발생하는 예외 처리를 테스트합니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testGetNotificationById_NotFound() {
        // Mock 설정 (빈 Optional 반환)
        when(notificationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // 예외 발생 여부 확인
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotificationById(1L));
    }

    /**
     * 알림을 저장 또는 업데이트하는 테스트입니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testSaveOrUpdateNotification() {
        // Given
        Notification notification = new Notification("New notification");

        // Mock 설정
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        Notification result = notificationService.saveOrUpdateNotification(notification);

        // Then
        assertNotNull(result);
        assertEquals("New notification", result.getMessage());
    }

    /**
     * 알림을 읽음 상태로 변경하는 테스트입니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testMarkNotificationAsRead() {
        // Given
        Notification notification = new Notification("Test notification");
        notification.setRead(false);

        // Mock 설정
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        Notification result = notificationService.markNotificationAsRead(1L);

        // Then
        assertTrue(result.isRead());
    }

    /**
     * 읽지 않은 알림 목록을 조회하는 테스트입니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testGetUnreadNotifications() {
        // Given
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Unread notification"));

        // Mock 설정
        when(notificationRepository.findByIsReadFalseAndUserId(1L)).thenReturn(notifications);

        // When
        List<Notification> result = notificationService.getUnreadNotifications(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("Unread notification", result.get(0).getMessage());
    }

    /**
     * 사용자별 알림 개수를 조회하는 테스트입니다.
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testCountNotificationsByUserId() {
        // Mock 설정
        when(notificationRepository.countByUserId(1L)).thenReturn(5L);

        // When
        long result = notificationService.countNotificationsByUserId(1L);

        // Then
        assertEquals(5, result);
    }
}
