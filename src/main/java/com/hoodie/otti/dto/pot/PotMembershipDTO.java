package com.hoodie.otti.dto.pot;

import com.hoodie.otti.dto.ott.OttResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PotMembershipDTO {
    private Long id;
    private Long potId;
    private String potName;
    private OttResponseDto ott;
    private PotMembershipUserDTO user;
    private Boolean approved;
    private Boolean hasPermission;

    public PotMembershipDTO(Long id, Long potId, String potName, OttResponseDto ott, PotMembershipUserDTO user, boolean approved, boolean hasPermission) {
        this.id = id;
        this.potId = potId;
        this.potName = potName;
        this.ott = ott;
        this.user = user;
        this.approved = approved;
        this.hasPermission = hasPermission;
    }
}
