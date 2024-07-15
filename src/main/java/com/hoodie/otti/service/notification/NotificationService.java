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

    /**
     * NotificationRepository를 주입받는 생성자입니다.
     * @param notificationRepository 알림 관련 데이터 액세스를 처리하는 리포지토리
     */
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * 모든 알림을 조회하는 메서드입니다.
     * @return 모든 알림 목록
     */
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * 주어진 userId에 해당하는 알림을 조회하는 메서드입니다.
     * @param userId 사용자 ID
     * @return 해당하는 알림 객체
     * @throws NotificationNotFoundException 주어진 userId에 해당하는 알림을 찾을 수 없을 경우 발생하는 예외
     */
    public Notification getNotificationById(Long userId) {
        return notificationRepository.findById(userId)
                .orElseThrow(() -> new NotificationNotFoundException("해당하는 알림을 찾을 수 없습니다: " + userId));
    }

    /**
     * 새로운 알림을 저장하거나 기존 알림을 업데이트하는 메서드입니다.
     * @param notification 저장 또는 업데이트할 알림 객체
     * @return 저장 또는 업데이트된 알림 객체
     */
    public Notification saveOrUpdateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * 주어진 userId에 해당하는 알림을 삭제하는 메서드입니다.
     * @param userId 사용자 ID
     */
    public void deleteNotification(Long userId) {
        notificationRepository.deleteById(userId);
    }

    /**
     * 주어진 userId에 해당하는 알림을 읽음 처리하는 메서드입니다.
     * @param userId 사용자 ID
     * @return 읽음 처리된 알림 객체
     * @throws NotificationNotFoundException 주어진 userId에 해당하는 알림을 찾을 수 없을 경우 발생하는 예외
     */
    public Notification markNotificationAsRead(Long userId) {
        Notification notification = getNotificationById(userId);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    /**
     * 주어진 userId에 해당하는 읽지 않은 알림 목록을 조회하는 메서드입니다.
     * @param userId 사용자 ID
     * @return 읽지 않은 알림 목록
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByIsReadFalseAndUserId(userId);
    }

    /**
     * 주어진 userId에 해당하는 알림의 개수를 조회하는 메서드입니다.
     * @param userId 사용자 ID
     * @return 알림의 개수
     */
    public long countNotificationsByUserId(Long userId) {
        return notificationRepository.countByUserId(userId);
    }
}
