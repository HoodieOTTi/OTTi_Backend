package com.hoodie.otti.dto.ott;

import com.hoodie.otti.model.ott.Ott;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class OttResponseDto {

    private Long id;
    private String name;
    private String ratePlan;
    private Integer price;
    private String image;
    private Date createdDate;
    private Date modifiedDate;

    @Builder
    public OttResponseDto(Long id, String name, String ratePlan, Integer price, String image, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.name = name;
        this.ratePlan = ratePlan;
        this.price = price;
        this.image = image;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static OttResponseDto fromEntity(Ott entity) {
        return OttResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ratePlan(entity.getRatePlan())
                .price(entity.getPrice())
                .image(entity.getImage())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
