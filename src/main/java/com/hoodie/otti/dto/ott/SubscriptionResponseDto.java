package com.hoodie.otti.dto.ott;

import com.hoodie.otti.model.Subscription;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscriptionResponseDto {

    private Long id;
    private Integer payment;
    private String memo;
    private Date paymentDate;
    private Long userId;
    private OttResponseDto ott;
    private Date createdDate;
    private Date modifiedDate;

    @Builder
    public SubscriptionResponseDto(Subscription subscription) {
        this.id = subscription.getId();
        this.payment = subscription.getPayment();
        this.memo = subscription.getMemo();
        this.paymentDate = subscription.getPaymentDate();
        this.userId = subscription.getUserId().getId();
        this.ott = new OttResponseDto(subscription.getOttId());
        this.createdDate = subscription.getCreatedDate();
        this.modifiedDate = subscription.getModifiedDate();
    }
}
