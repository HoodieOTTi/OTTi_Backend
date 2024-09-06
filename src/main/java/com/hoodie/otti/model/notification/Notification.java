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
    @JsonProperty("userId")
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

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(String message) {
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(Long userId, String message, boolean isRead, LocalDateTime createdAt) {
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
