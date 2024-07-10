package com.hoodie.otti.dto.ott;

import com.hoodie.otti.model.ott.Subscription;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscriptionByUserResponseDto {

    private Long id;
    private String name;
    private Integer payment;
    private String memo;
    private Integer paymentDate;
    private OttResponseDto ott;
    private Date createdDate;
    private Date modifiedDate;

    @Builder
    public SubscriptionByUserResponseDto(Subscription subscription) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.payment = subscription.getPayment();
        this.memo = subscription.getMemo();
        this.paymentDate = subscription.getPaymentDate();
        this.ott = new OttResponseDto(subscription.getOttId());
        this.createdDate = subscription.getCreatedDate();
        this.modifiedDate = subscription.getModifiedDate();
    }
}
