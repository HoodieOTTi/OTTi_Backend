package com.hoodie.otti.model.profile;

import com.hoodie.otti.model.community.Likes;
import com.hoodie.otti.model.community.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "닉네임은 필수 입력 사항입니다.")
    @Size(min = 2, max = 50, message = "닉네임은 최소 2자에서 최대 50자여야 합니다.")
    private String username;

    private String userEmail;

    @Column(unique = true)
    private Long kakaoId;

    private String profilePhotoUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Likes> likes;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User() {
    }

    public User(String username, String profilePhotoUrl, String userEmail) {
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
        this.userEmail = userEmail;
    }

    public User(Long kakaoId, String username, String userEmail) {
        this.kakaoId = kakaoId;
        this.username = username;
        this.userEmail = userEmail;
    }

    public User(Long id){
        this.id = id;
    }
}
