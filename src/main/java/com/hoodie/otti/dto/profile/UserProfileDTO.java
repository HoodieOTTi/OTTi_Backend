package com.hoodie.otti.dto.profile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UserProfileDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    private String profilePhotoUrl;


    @JsonCreator
    public UserProfileDTO(
            @JsonProperty("username") String username,
            @JsonProperty("profilePhotoUrl") String profilePhotoUrl) {
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
