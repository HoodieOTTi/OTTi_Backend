package com.hoodie.otti.dto.pot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequesterDTO {
    private Long id;
    private String username;
    private String profilePhotoUrl;
    private String joinrequestDescription;

    @Builder
    public RequesterDTO(Long id, String username, String profilePhotoUrl, String joinrequestDescription) {
        this.id = id;
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
        this.joinrequestDescription = joinrequestDescription;
    }
}
