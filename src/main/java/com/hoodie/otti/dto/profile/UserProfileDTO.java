package com.hoodie.otti.dto.profile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {

    private String username;

    private String profilePhotoUrl;


    @JsonCreator
    public UserProfileDTO(
            @JsonProperty("username") String username,
            @JsonProperty("profilePhotoUrl") String profilePhotoUrl) {
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
