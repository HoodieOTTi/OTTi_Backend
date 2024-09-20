package com.hoodie.otti.dto.pot;

import com.hoodie.otti.model.pot.JoinRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDTO {
    private Long id;
    private String potName;
    private RequesterDTO requester;
    private Boolean approved;

    @Builder
    public JoinRequestDTO(Long id, String potName, RequesterDTO requester, Boolean approved) {
        this.id = id;
        this.potName = potName;
        this.requester = requester;
        this.approved = approved;
    }

    public static JoinRequestDTO fromEntity(JoinRequest joinRequest) {
        return JoinRequestDTO.builder()
                .id(joinRequest.getId())
                .potName(joinRequest.getPot().getName())
                .requester(new RequesterDTO(
                        joinRequest.getRequester().getUsername(),
                        joinRequest.getRequester().getProfilePhotoUrl(),
                        joinRequest.getJoinrequestDescription()))
                .approved(joinRequest.getApproved())
                .build();
    }
}

