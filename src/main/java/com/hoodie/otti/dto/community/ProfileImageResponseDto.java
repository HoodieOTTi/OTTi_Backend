package com.hoodie.otti.dto.community;

import lombok.Getter;

@Getter
public class ProfileImageResponseDto {

    private String imageUrl;

    public ProfileImageResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
