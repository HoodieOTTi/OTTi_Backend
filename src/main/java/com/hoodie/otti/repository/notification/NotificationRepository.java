package com.hoodie.otti.repository.notification;

import com.hoodie.otti.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByIsReadFalseAndUserId(Long userId);

    long countByUserId(Long userId);
}
