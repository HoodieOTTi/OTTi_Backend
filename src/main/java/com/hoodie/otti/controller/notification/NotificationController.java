package com.hoodie.otti.controller.notification;

import com.hoodie.otti.exception.notification.NotificationNotFoundException;
import com.hoodie.otti.model.notification.Notification;
import com.hoodie.otti.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // 특정 ID의 알림을 읽음으로 표시하는 엔드포인트
    @PostMapping("/{id}/mark-as-read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id) {
        try {
            Notification notification = notificationService.markNotificationAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // 읽지 않은 모든 알림을 조회하는 엔드포인트
    @GetMapping("/unread/{id}")
    public List<Notification> getUnreadNotifications(@PathVariable Long id) {
        return notificationService.getUnreadNotifications(id);
    }

    // 특정 사용자의 알림 수를 조회하는 엔드포인트
    @GetMapping("/user/{id}/count")
    public long countNotificationsByUserId(@PathVariable Long id) {
        return notificationService.countNotificationsByUserId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (notificationService.existsById(id)) {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
