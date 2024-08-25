package com.hoodie.otti.dto.pot;

import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.pot.Pot;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PotSaveRequestDto {

    private String name;  // 팟 이름
//    private Long userId;  // 사용자 ID
    private Long ottId;   // OTT ID
    private String depositAccount;  // 입금계좌
    private String ratePlan;  // 결제일
//    private Long creatorId;  // 생성자 ID

//    @Builder
//    public PotSaveRequestDto(String name, Long userId, Long ottId, String depositAccount, String ratePlan, Long creatorId) {
//        this.name = name;
//        this.userId = userId;
//        this.ottId = ottId;
//        this.depositAccount = depositAccount;
//        this.ratePlan = ratePlan;
//        this.creatorId = creatorId;
//    }

    @Builder
    public PotSaveRequestDto(String name, Long ottId, String depositAccount, String ratePlan) {
        this.name = name;
        this.ottId = ottId;
        this.depositAccount = depositAccount;
        this.ratePlan = ratePlan;
    }


    // Pot 엔티티로 변환하는 메서드
//    public Pot toEntity(User user, Ott ott) {
//        return Pot.builder()
//                .name(name)
//                .user(user)
//                .ott(ott)
//                .depositAccount(depositAccount)
//                .ratePlan(ratePlan)
//                .creator(user)
//                .build();
//    }

    public Pot toEntity(Ott ott) {
        return Pot.builder()
                .name(name)
                .ott(ott)
                .depositAccount(depositAccount)
                .ratePlan(ratePlan)
                .build();
    }
}

