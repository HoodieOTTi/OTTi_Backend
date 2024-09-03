package com.hoodie.otti.dto.ott;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OttBaseResponseDto {

    private Long id;
    private String name;
    private String ratePlan;
    private Integer price;
    private String image;

    @Builder
    public OttBaseResponseDto(Long id, String name, String ratePlan, Integer price, String image) {
        this.id = id;
        this.name = name;
        this.ratePlan = ratePlan;
        this.price = price;
        this.image = image;
    }
}
