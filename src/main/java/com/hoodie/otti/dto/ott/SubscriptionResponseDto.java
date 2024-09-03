package com.hoodie.otti.dto.ott;

import com.hoodie.otti.model.ott.Subscription;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscriptionResponseDto {

    private Long id;
    private String name;
    private Integer payment;
    private String memo;
    private Integer paymentDate;
    private Long userId;
    private OttResponseDto ott;
    private String createdDate;
    private String modifiedDate;

    @Builder
    public SubscriptionResponseDto(Subscription subscription) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.payment = subscription.getPayment();
        this.memo = subscription.getMemo();
        this.paymentDate = subscription.getPaymentDate();
        this.userId = subscription.getUserId().getId();
        this.ott = new OttResponseDto(subscription.getOttId());
        this.createdDate = subscription.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = subscription.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
