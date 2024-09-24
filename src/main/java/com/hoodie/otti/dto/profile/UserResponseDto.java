package com.hoodie.otti.dto.profile;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private String userName;
    private String userprofilePhotoUrl;

    public UserResponseDto(String userName, String userprofilePhotoUrl) {
        this.userName = userName;
        this.userprofilePhotoUrl = userprofilePhotoUrl;
    }
}
