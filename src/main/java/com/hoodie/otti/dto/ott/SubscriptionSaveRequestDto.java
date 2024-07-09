package com.hoodie.otti.dto.ott;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionSaveRequestDto {

    private String name;
    private Integer payment;
    private String memo;
    private Integer paymentDate;
    private Long userProfileId;
    private Long ottId;

    @Builder
    public SubscriptionSaveRequestDto(String name, Integer payment, String memo, Integer paymentDate,
                                      Long userProfileId, Long ottId) {
        this.name = name;
        this.payment = payment;
        this.memo = memo;
        this.paymentDate = paymentDate;
        this.userProfileId = userProfileId;
        this.ottId = ottId;
    }
}
