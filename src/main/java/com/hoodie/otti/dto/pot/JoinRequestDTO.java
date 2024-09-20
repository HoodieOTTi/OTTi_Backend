package com.hoodie.otti.dto.pot;

import com.hoodie.otti.dto.ott.OttResponseDto;
import com.hoodie.otti.model.pot.JoinRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDTO {
    private Long id;
    private String potName;
    private OttResponseDto ott;
    private RequesterDTO requester;
    private Boolean approved;

    @Builder
    public JoinRequestDTO(Long id, String potName, OttResponseDto ott, RequesterDTO requester, Boolean approved) {
        this.id = id;
        this.potName = potName;
        this.ott = ott;
        this.requester = requester;
        this.approved = approved;
    }

    public static JoinRequestDTO fromEntity(JoinRequest joinRequest) {
        OttResponseDto ottResponseDto = OttResponseDto.fromEntity(joinRequest.getPot().getOttId());

        return JoinRequestDTO.builder()
                .id(joinRequest.getId())
                .potName(joinRequest.getPot().getName())
                .ott(ottResponseDto)
                .requester(new RequesterDTO(
                        joinRequest.getRequester().getUsername(),
                        joinRequest.getRequester().getProfilePhotoUrl(),
                        joinRequest.getJoinrequestDescription()))
                .approved(joinRequest.getApproved())
                .build();
    }
}

