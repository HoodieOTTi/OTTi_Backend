package com.hoodie.otti.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class UserProfileDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Nickname cannot be blank")
    private String nickname;

    private String profilePhotoUrl;

    // 생성자
    public UserProfileDTO(String username, String nickname, String profilePhotoUrl) {
        this.username = username;
        this.nickname = nickname;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    // Getter 및 Setter 메서드
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}

