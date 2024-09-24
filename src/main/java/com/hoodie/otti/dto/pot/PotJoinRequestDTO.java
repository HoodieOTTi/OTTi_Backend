package com.hoodie.otti.dto.pot;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.model.ott.Ott;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PotJoinRequestDTO {
    private Long id;
    private String potName;
    private Ott ott;
    private String depositAccount;
    private String ratePlan;
    private int memberCount;
    private UserProfileDTO creator;
    private List<JoinRequestDTO> joinRequests;
}
