package com.hoodie.otti.dto.ott;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionUpdateRequestDto {

    private String name;
    private Integer payment;
    private String memo;
    private Date paymentDate;
    private Long ottId;

    @Builder
    public SubscriptionUpdateRequestDto(String name, Integer payment, String memo, Date paymentDate, Long ottId) {
        this.name = name;
        this.payment = payment;
        this.memo = memo;
        this.paymentDate = paymentDate;
        this.ottId = ottId;
    }
}
