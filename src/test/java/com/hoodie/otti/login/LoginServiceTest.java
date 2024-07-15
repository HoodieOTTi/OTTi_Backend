package com.hoodie.otti.login;

import com.hoodie.otti.dto.login.UserKakaoLoginResponseDto;
import com.hoodie.otti.dto.login.UserKakaoSignupRequestDto;
import com.hoodie.otti.dto.login.UserResponseDto;
import com.hoodie.otti.repository.login.UserRepository;
import com.hoodie.otti.service.login.LoginService;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        // Mock 객체 초기화 및 설정
        loginService = new LoginService(userRepository, jwtTokenProvider, restTemplate);
    }

    @Test
    void testKakaoLogin() throws LoginService.BaseException {
        // Given
        String accessToken = "dummy_access_token";
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("id", "123456");
        userInfo.put("nickname", "testUser");
        userInfo.put("email", "test@example.com");

        UserResponseDto mockUserResponseDto = new UserResponseDto();
        mockUserResponseDto.setUserEmail("test@example.com");

        when(loginService.getUserKaKaoAccessInfo(anyString())).thenReturn(userInfo);
        when(loginService.findByUserKakaoIdentifier(anyString())).thenReturn(null);
        when(loginService.signUp(any(UserKakaoSignupRequestDto.class))).thenReturn(1L);
        when(jwtTokenProvider.createToken(anyString())).thenReturn("dummy_token");

        // When
        UserKakaoLoginResponseDto responseDto = loginService.kakaoLogin(accessToken);

        // Then
        assertEquals("dummy_token", responseDto.getToken());
        assertEquals("test@example.com", responseDto.getUserEmail());
    }

    @Test
    void testGetKaKaoAccessToken() {
        // Given
        String code = "dummy_code";
        String accessToken = "dummy_access_token";

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(null);

        // When
        String result = loginService.getKaKaoAccessToken(code);

        // Then
        assertEquals(accessToken, result);
    }

    // 추가적인 테스트 케이스들을 여기에 작성할 수 있습니다.
}
