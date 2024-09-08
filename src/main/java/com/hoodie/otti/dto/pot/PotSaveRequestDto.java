package com.hoodie.otti.dto.pot;

import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PotSaveRequestDto {

    private String name;
    private Long userId;
    private String ottName;
    private String ottRatePlan;
    private String depositAccount;
    private String ratePlan;
    private Long creatorId;

    @Builder
    public PotSaveRequestDto(String name, Long userId, String ottName, String ottRatePlan,
                             String depositAccount, String ratePlan, Long creatorId) {
        this.name = name;
        this.userId = userId;
        this.ottName = ottName;
        this.ottRatePlan = ottRatePlan;
        this.depositAccount = depositAccount;
        this.ratePlan = ratePlan;
        this.creatorId = creatorId;
    }

    public Pot toEntity(User user, Ott ott, User creator) {
        return Pot.builder()
                .name(name)
                .user(user)
                .ott(ott)
                .depositAccount(depositAccount)
                .ratePlan(ratePlan)
                .creator(creator)
                .build();
    }
}
