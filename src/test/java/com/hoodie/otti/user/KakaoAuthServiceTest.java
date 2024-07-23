package com.hoodie.otti.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.dto.login.KakaoInfo;
import com.hoodie.otti.dto.login.MemberResponse;
import com.hoodie.otti.service.user.OAuthService;
import com.hoodie.otti.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class KakaoAuthServiceTest {

    @InjectMocks
    private OAuthService oAuthService;

    @Mock
    private UserService userService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "USER")
    public void testGetAccessToken() throws Exception {
        // 준비
        String code = "test_code";
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        String responseBody = "{\"access_token\":\"test_access_token\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "test_client_id");
        body.add("redirect_uri", "test_redirect_uri");
        body.add("client_secret", "test_client_secret");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(tokenUri, HttpMethod.POST, kakaoTokenRequest, String.class)).thenReturn(responseEntity);
        when(objectMapper.readTree(responseBody)).thenReturn(objectMapper.readTree(responseBody));

        // 테스트
        String accessToken = oAuthService.getAccessToken(code);
        assertEquals("test_access_token", accessToken);
    }

    @Test
    public void testGetKakaoInfo() throws Exception {
        // 준비
        String accessToken = "test_access_token";
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        String responseBody = "{\"id\":12345, \"kakao_account\": {\"email\": \"test@example.com\"}, \"properties\": {\"nickname\": \"test_user\"}}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(userInfoUri, HttpMethod.GET, kakaoUserInfoRequest, String.class)).thenReturn(responseEntity);
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        when(objectMapper.readTree(responseBody)).thenReturn(jsonNode);

        // 테스트
        KakaoInfo kakaoInfo = oAuthService.getKakaoInfo(accessToken);
        assertNotNull(kakaoInfo);
        assertEquals(12345, kakaoInfo.getId());
        assertEquals("test@example.com", kakaoInfo.getEmail());
        assertEquals("test_user", kakaoInfo.getNickname());
    }

    @Test
    public void testLoginUser() throws Exception {
        // 준비
        String accessToken = "test_access_token";
        KakaoInfo kakaoInfo = new KakaoInfo(12345L, "test_user", "test@example.com");

        when(oAuthService.getKakaoInfo(accessToken)).thenReturn(kakaoInfo);

        HttpSession session = mock(HttpSession.class);

        oAuthService.loginUser(accessToken, session);

        verify(session).setAttribute("loginMember", new MemberResponse(12345L, "test_user", "test@example.com"));
    }
}
