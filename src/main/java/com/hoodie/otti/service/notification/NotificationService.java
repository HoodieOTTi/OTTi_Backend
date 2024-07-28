package com.hoodie.otti.service.notification;

import com.hoodie.otti.model.notification.Notification;
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

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return notificationRepository.existsById(id);
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
