package com.hoodie.otti.dto.pot;

import com.hoodie.otti.dto.profile.UserProfileDTO;
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
    private String potDescription;
    private UserProfileDTO creator;
    private List<JoinRequestDTO> joinRequests;
}
