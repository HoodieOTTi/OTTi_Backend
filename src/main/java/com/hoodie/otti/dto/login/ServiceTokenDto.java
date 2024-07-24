package com.hoodie.otti.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ServiceTokenDto {

    @JsonProperty("access_token")
    private String accessToken;
}
