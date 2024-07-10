package com.hoodie.otti.dto.ott;

import lombok.Getter;

@Getter
public class SubscriptionDDayResponseDto {

    private Integer dDay;

    public SubscriptionDDayResponseDto(Integer dDay) {
        this.dDay = dDay;
    }
}
