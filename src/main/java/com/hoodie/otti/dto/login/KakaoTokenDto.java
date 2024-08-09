package com.hoodie.otti.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KakaoTokenDto {

    @SerializedName("access_token")
    private String accessToken;

//    @JsonProperty("token_type")
//    private String tokenType;

    @SerializedName("refresh_token")
    private String refreshToken;

//    @JsonProperty("expires_in")
//    private int expiresIn;
//
//    @JsonProperty("scope")
//    private String scope;

    @Data
    @Builder
    public static class ServiceToken{
        private String accessToken;
        private String refreshToken;
    }
}
