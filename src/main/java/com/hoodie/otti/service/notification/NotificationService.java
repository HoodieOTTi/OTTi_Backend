package com.hoodie.otti.service.notification;

import com.hoodie.otti.entity.notification.Notification;
import com.hoodie.otti.exception.notification.NotificationNotFoundException;
import com.hoodie.otti.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("해당하는 알림을 찾을 수 없습니다: " + notificationId));
    }

    public Notification saveOrUpdateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new NotificationNotFoundException("해당하는 알림을 찾을 수 없습니다: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }

    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = getNotificationById(notificationId);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByIsReadFalseAndUserId(userId);
    }

    public long countNotificationsByUserId(Long userId) {
        return notificationRepository.countByUserId(userId);
    }
}
