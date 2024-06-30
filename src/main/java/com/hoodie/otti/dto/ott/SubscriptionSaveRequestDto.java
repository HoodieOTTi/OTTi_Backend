package com.hoodie.otti.dto.ott;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionSaveRequestDto {

    private Integer payment;
    private String memo;
    private Date paymentDate;
    private Long userId;
    private Long ottId;

    @Builder
    public SubscriptionSaveRequestDto(Integer payment, String memo, Date paymentDate,Long userId, Long ottId) {
        this.payment = payment;
        this.memo = memo;
        this.paymentDate = paymentDate;
        this.userId = userId;
        this.ottId = ottId;
    }
}
