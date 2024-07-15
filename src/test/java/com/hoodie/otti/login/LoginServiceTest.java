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

    /**
     * 각 테스트 메서드 실행 전 LoginService 인스턴스를 초기화합니다.
     */
    @BeforeEach
    void setUp() {
        // Mock 객체 초기화 및 설정
        loginService = new LoginService(userRepository, jwtTokenProvider, restTemplate);
    }

    /**
     * kakaoLogin 메서드의 성공적인 동작을 테스트합니다.
     *
     * @throws LoginService.BaseException 예외 처리 시 처리됩니다.
     */
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

        // loginService.getUserKaKaoAccessInfo 메서드의 반환 값을 모의 설정합니다.
        when(loginService.getUserKaKaoAccessInfo(anyString())).thenReturn(userInfo);

        // loginService.findByUserKakaoIdentifier 메서드의 반환 값을 모의 설정합니다.
        when(loginService.findByUserKakaoIdentifier(anyString())).thenReturn(null);

        // loginService.signUp 메서드의 반환 값을 모의 설정합니다.
        when(loginService.signUp(any(UserKakaoSignupRequestDto.class))).thenReturn(1L);

        // jwtTokenProvider.createToken 메서드의 반환 값을 모의 설정합니다.
        when(jwtTokenProvider.createToken(anyString())).thenReturn("dummy_token");

        // When
        UserKakaoLoginResponseDto responseDto = loginService.kakaoLogin(accessToken);

        // Then
        assertEquals("dummy_token", responseDto.getToken());
        assertEquals("test@example.com", responseDto.getUserEmail());
    }

    /**
     * getKaKaoAccessToken 메서드의 동작을 테스트합니다.
     */
    @Test
    void testGetKaKaoAccessToken() {
        // Given
        String code = "dummy_code";
        String accessToken = "dummy_access_token";

        // restTemplate.postForEntity 메서드의 반환 값을 모의 설정합니다.
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(null);

        // When
        String result = loginService.getKaKaoAccessToken(code);

        // Then
        assertEquals(accessToken, result);
    }

}
