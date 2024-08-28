package com.hoodie.otti.service.notification;

import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.model.notification.Notification;
import com.hoodie.otti.model.pot.JoinRequest;
import com.hoodie.otti.model.pot.Pot;
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


    // pot 관련 알림
    public void sendPotJoinRequestNotification(Pot pot, JoinRequest joinRequest) {
        // 'pot'의 권한이 있는 사용자에게 가입 신청 알림 전송
    }

    public void sendJoinApprovalNotification(User user, Pot pot) {
        // 사용자에게 가입 승인 알림 전송
    }

    public void sendJoinRejectionNotification(User user, Pot pot) {
        // 사용자에게 가입 거절 알림 전송
    }


}
