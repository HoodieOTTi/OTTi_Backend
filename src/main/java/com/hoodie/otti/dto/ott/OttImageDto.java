package com.hoodie.otti.dto.ott;

import lombok.Getter;

@Getter
public class OttImageDto {

    private String image;

    public OttImageDto(String image) {
        this.image = image;
    }
}
