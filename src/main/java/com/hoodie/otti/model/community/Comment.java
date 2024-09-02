package com.hoodie.otti.model.community;

import com.hoodie.otti.model.profile.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public Comment(String text, LocalDateTime createdDate, LocalDateTime modifiedDate, Post post, User user) {
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.post = post;
        this.user = user;
    }

    public void update(String text) {
        if (!isNullAndBlank(text)) {
            this.text = text;
        }
        this.modifiedDate = LocalDateTime.now();
    }

    private <T> boolean isNullAndBlank(T argument) {
        if (argument instanceof String) {
            return argument == null || ((String) argument).trim().isEmpty();
        }

        return argument == null;
    }
}
