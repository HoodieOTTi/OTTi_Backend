package com.hoodie.otti.dto.community;

import lombok.Getter;

@Getter
public class ImageResponseDto {

    private Long id;
    private String url;

    public ImageResponseDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }
}
