package com.hoodie.otti.dto.community;

import lombok.Getter;

@Getter
public class ImageResponseDto {

    private Long id;
    private String imageUrl;

    public ImageResponseDto(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
