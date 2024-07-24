package com.hoodie.otti.model.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
