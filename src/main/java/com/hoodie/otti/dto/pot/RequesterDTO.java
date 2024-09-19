package com.hoodie.otti.dto.pot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequesterDTO {
    private String username;
    private String profilePhotoUrl;
    private String joinrequestDescription;

    @Builder
    public RequesterDTO(String username, String profilePhotoUrl, String joinrequestDescription) {
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
        this.joinrequestDescription = joinrequestDescription;
    }
}
