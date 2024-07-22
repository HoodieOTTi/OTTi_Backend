package com.hoodie.otti.dto.ott;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionUpdateRequestDto {

    private String name;
    private Integer payment;
    private String memo;
    private Integer paymentDate;
    private String ottName;
    private String ottRatePlan;

    @Builder
    public SubscriptionUpdateRequestDto(String name, Integer payment, String memo, Integer paymentDate,
                                        String ottName, String ottRatePlan) {
        this.name = name;
        this.payment = payment;
        this.memo = memo;
        this.paymentDate = paymentDate;
        this.ottName = ottName;
        this.ottRatePlan = ottRatePlan;
    }
}
