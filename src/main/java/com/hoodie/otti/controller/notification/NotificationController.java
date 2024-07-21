package com.hoodie.otti.controller.notification;

import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.exception.notification.NotificationNotFoundException;
import com.hoodie.otti.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 모든 알림을 조회하는 엔드포인트
     *
     * @return 모든 알림 목록
     */
    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    /**
     * 새로운 알림을 생성하는 엔드포인트
     *
     * @param notification 생성할 알림 정보
     * @return 생성된 알림 객체
     */
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.saveOrUpdateNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification); // 상태 코드 201 반환
    }

    /**
     * 특정 ID의 알림을 조회하는 엔드포인트
     *
     * @param id 조회할 알림의 ID
     * @return 조회된 알림 객체
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    /**
     * 특정 ID의 알림을 읽음으로 표시하는 엔드포인트
     *
     * @param id 읽음으로 표시할 알림의 ID
     * @return 읽음으로 표시된 알림 객체
     */
    @PostMapping("/{id}/mark-as-read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id) {
        try {
            Notification notification = notificationService.markNotificationAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    /**
     * 읽지 않은 모든 알림을 조회하는 엔드포인트
     *
     * @param id 사용자 ID
     * @return 읽지 않은 모든 알림 목록
     */
    @GetMapping("/unread/{id}")
    public List<Notification> getUnreadNotifications(@PathVariable Long id) {
        return notificationService.getUnreadNotifications(id);
    }

    /**
     * 특정 사용자의 알림 수를 조회하는 엔드포인트
     *
     * @param id 사용자 ID
     * @return 해당 사용자의 알림 수
     */
    @GetMapping("/user/{id}/count")
    public long countNotificationsByUserId(@PathVariable Long id) {
        return notificationService.countNotificationsByUserId(id);
    }
}
