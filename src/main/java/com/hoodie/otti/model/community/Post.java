package com.hoodie.otti.model.community;

import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Column(length = 40, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Image> images;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "POT_ID", nullable = false)
    private Pot pot;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comment> comments;

    @Builder
    public Post(String title, String content, LocalDateTime createdDate,
                LocalDateTime modifiedDate, User user, Pot pot) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.user = user;
        this.pot = pot;
    }

    public void update(String title, String content, Optional<Pot> pot) {
        if (!isNullAndBlank(title)) {
            this.title = title;
        }
        if (!isNullAndBlank(content)) {
            this.content = content;
        }
        pot.ifPresent(p -> this.pot = p);
        this.modifiedDate = LocalDateTime.now();
    }

    private <T> boolean isNullAndBlank(T argument) {
        if (argument instanceof String) {
            return argument == null || ((String) argument).trim().isEmpty();
        }

        return argument == null;
    }
}
