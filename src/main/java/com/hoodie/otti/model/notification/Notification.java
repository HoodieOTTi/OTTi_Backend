package com.hoodie.otti.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId")  // JSON 필드 이름과 매핑
    private Long userId;

    @Column(nullable = false)
    @JsonProperty("isRead")
    private boolean isRead;

    @Column(length = 255)
    @JsonProperty("message")
    private String message;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    // 기본 생성자
    public Notification() {
        this.createdAt = LocalDateTime.now(); // 생성 시 자동으로 현재 시간으로 초기화
    }

    // 메시지를 받아 초기화하는 생성자
    public Notification(String message) {
        this.message = message;
        this.isRead = false; // 기본값은 읽지 않음
        this.createdAt = LocalDateTime.now(); // 현재 시간으로 초기화
    }

    // 전체 필드를 받아 초기화하는 생성자
    public Notification(Long userId, String message, boolean isRead, LocalDateTime createdAt) {
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
