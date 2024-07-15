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

    public Notification getNotificationById(Long userId) {
        return notificationRepository.findById(userId)
                .orElseThrow(() -> new NotificationNotFoundException("해당하는 알림을 찾을 수 없습니다: " + userId));
    }

    public Notification saveOrUpdateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long userId) {
        notificationRepository.deleteById(userId);
    }

    public Notification markNotificationAsRead(Long userId) {
        Notification notification = getNotificationById(userId);
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
