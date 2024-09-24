package com.hoodie.otti.dto.pot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PotMembershipUserDTO {
    private Long id;
    private String username;
    private String profilePhotoUrl;

    @Builder
    public PotMembershipUserDTO(Long id,String username, String profilePhotoUrl) {
        this.id = id;
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
