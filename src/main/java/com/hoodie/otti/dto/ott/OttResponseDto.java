package com.hoodie.otti.dto.ott;

import com.hoodie.otti.model.ott.Ott;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

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
    public OttResponseDto(Ott entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.ratePlan = entity.getRatePlan();
        this.price = entity.getPrice();
        this.image = entity.getImage();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
