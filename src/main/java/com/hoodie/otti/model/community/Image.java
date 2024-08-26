package com.hoodie.otti.model.community;

import com.hoodie.otti.model.pot.Pot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    private String imageName;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public Image(String imageName, String imageUrl, Post post) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.post = post;
    }

    public void mappingPost(Post post) {
        this.post = post;
    }

    public void update(String imageName, String imageUrl, Post post) {
        if (!isNullAndBlank(imageName)) {
            this.imageName = imageName;
        }
        if (!isNullAndBlank(imageUrl)) {
            this.imageUrl = imageUrl;
        }
        if (!isNullAndBlank(post)) {
            this.post = post;
        }
    }

    private <T> boolean isNullAndBlank(T argument) {
        if (argument instanceof String) {
            return argument == null || ((String) argument).trim().isEmpty();
        }

        return argument == null;
    }
}
