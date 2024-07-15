package com.hoodie.otti.service.login;

import com.hoodie.otti.dto.login.UserKakaoLoginResponseDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;

@Service
public class AuthService {

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.client_secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Kakao 로그인 URL을 생성하는 메서드
     *
     * @return URL 객체
     */
    public URL getKakaoLoginUrl() {
        try {
            URI uri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("redirect_uri", kakaoRedirectUri)
                    .queryParam("response_type", "code")
                    .build()
                    .toUri();

            return uri.toURL();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Kakao OAuth 인가 코드를 사용하여 액세스 토큰을 요청하는 메서드
     *
     * @param code Kakao OAuth 인가 코드
     * @return 액세스 토큰
     */
    public String getKakaoAccessToken(String code) {
        try {
            URI uri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/token")
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("client_secret", kakaoClientSecret)
                    .queryParam("redirect_uri", kakaoRedirectUri)
                    .queryParam("code", code)
                    .build()
                    .toUri();

            UserKakaoLoginResponseDto responseDto = restTemplate.postForObject(uri, null, UserKakaoLoginResponseDto.class);
            if (responseDto != null) {
                return responseDto.getToken();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
