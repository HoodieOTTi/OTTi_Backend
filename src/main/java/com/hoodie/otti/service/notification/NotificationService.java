package com.hoodie.otti.service.notification;

import com.hoodie.otti.model.notification.Notification;
import com.hoodie.otti.model.pot.JoinRequest;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.notification.NotificationRepository;
import com.hoodie.otti.repository.pot.PotMembershipRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PotMembershipRepository potMembershipRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, PotMembershipRepository potMembershipRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.potMembershipRepository = potMembershipRepository;
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

    public void sendPotJoinRequestNotification(Pot pot, JoinRequest joinRequest) {
        List<User> authorizedUsers = userRepository.findAll().stream()
                .filter(user -> potMembershipRepository.existsByUserAndPotAndHasPermission(user, pot, true))
                .collect(Collectors.toList());

        String message = String.format("%s님이 %s에 가입 신청을 했습니다.", joinRequest.getRequester().getUsername(), pot.getName());

        for (User user : authorizedUsers) {
            Notification notification = new Notification();
            notification.setUserId(user.getId());
            notification.setMessage(message);
            notification.setRead(false);
            saveNotification(notification);
        }
    }

    public void sendJoinApprovalNotification(User user, Pot pot) {
        String message = String.format("%s님의 가입 신청이 승인되었습니다. 포트: %s", user.getUsername(), pot.getName());
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setMessage(message);
        notification.setRead(false);
        saveNotification(notification);
    }

    public void sendJoinRejectionNotification(User user, Pot pot) {
        String message = String.format("%s님의 가입 신청이 거절되었습니다. 포트: %s", user.getUsername(), pot.getName());
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setMessage(message);
        notification.setRead(false);
        saveNotification(notification);
    }



}
