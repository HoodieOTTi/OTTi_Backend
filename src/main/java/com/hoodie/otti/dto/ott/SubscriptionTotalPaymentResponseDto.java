package com.hoodie.otti.dto.ott;

import lombok.Getter;

@Getter
public class SubscriptionTotalPaymentResponseDto {

    private Integer totalPayment;

    public SubscriptionTotalPaymentResponseDto(Integer totalPayment) {
        this.totalPayment = totalPayment;
    }
}
