package com.hoodie.otti.entity.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long userId;

    @Column(nullable = false) // 'is_read' 컬럼을 매핑합니다. NULL 값 허용하지 않음.
    @JsonProperty("isRead")
    private boolean isRead;

    @Column(length = 255)
    @JsonProperty("message")// 'message' 컬럼을 매핑합니다. 최대 길이는 255자입니다.
    private String message;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // 'created_at' 컬럼을 매핑합니다. NULL 값 허용하지 않음.
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    // 기본 생성자 (JPA 규약을 따라야 함)
    public Notification() {
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
