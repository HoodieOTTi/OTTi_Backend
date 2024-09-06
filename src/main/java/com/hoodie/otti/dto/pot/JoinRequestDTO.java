package com.hoodie.otti.dto.pot;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.model.pot.JoinRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDTO {
    private Long id;
    private Long potId;
    private UserProfileDTO requester;
    private Boolean approved;

    @Builder
    public JoinRequestDTO(Long id, Long potId, UserProfileDTO requester, Boolean approved) {
        this.id = id;
        this.potId = potId;
        this.requester = requester;
        this.approved = approved;
    }

    public static JoinRequestDTO fromEntity(JoinRequest joinRequest) {
        return JoinRequestDTO.builder()
                .id(joinRequest.getId())
                .potId(joinRequest.getPot().getId())
                .requester(new UserProfileDTO(
                        joinRequest.getRequester().getUsername(),
                        joinRequest.getRequester().getProfilePhotoUrl()))
                .approved(joinRequest.getApproved())
                .build();
    }
}

