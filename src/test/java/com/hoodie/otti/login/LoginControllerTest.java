package com.hoodie.otti.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.controller.login.LoginController;
import com.hoodie.otti.dto.login.BaseResponse;
import com.hoodie.otti.dto.login.UserKakaoLoginResponseDto;
import com.hoodie.otti.dto.login.UserResponseDto;
import com.hoodie.otti.dto.login.UserKakaoSignupRequestDto;
import com.hoodie.otti.service.login.LoginService;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper;

    /**
     * 각 테스트 메서드 실행 전 ObjectMapper를 초기화합니다.
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * KakaoCallback 메서드의 성공적인 동작을 테스트합니다.
     *
     * @throws Exception 예외 발생 시 처리됩니다.
     */
    @Test
    public void testKakaoCallback_Success() throws Exception {
        // Given
        String mockAccessToken = "mockAccessToken";
        String mockEmail = "test@example.com";
        UserKakaoLoginResponseDto mockResponseDto = new UserKakaoLoginResponseDto(HttpStatus.OK, "mockToken", mockEmail);

        // loginService.getKaKaoAccessToken 메서드의 반환 값을 모의 설정합니다.
        when(loginService.getKaKaoAccessToken(anyString())).thenReturn(mockAccessToken);

        // loginService.kakaoLogin 메서드의 반환 값을 모의 설정합니다.
        when(loginService.kakaoLogin(mockAccessToken)).thenReturn(mockResponseDto);

        // jwtTokenProvider.createToken 메서드의 반환 값을 모의 설정합니다.
        when(jwtTokenProvider.createToken(mockEmail)).thenReturn("mockToken");

        // When
        ResponseEntity<BaseResponse<UserKakaoLoginResponseDto>> responseEntity = loginController.kakaoCallback("mockAuthCode");

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        BaseResponse<UserKakaoLoginResponseDto> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("요청에 성공했습니다.", responseBody.getMessage());
        assertEquals(mockEmail, responseBody.getData().getUserEmail());
        assertEquals("mockToken", responseBody.getData().getToken());

        // jwtTokenProvider.createToken 메서드가 올바른 인자와 함께 호출되었음을 검증합니다.
        verify(jwtTokenProvider, times(1)).createToken(mockEmail);
    }

    /**
     * KakaoCallback 메서드에서 예외가 발생할 경우의 처리를 테스트합니다.
     *
     * @throws Exception 예외 발생 시 처리됩니다.
     */
    @Test
    public void testKakaoCallback_ExceptionHandling() throws Exception {
        // Given
        String mockAuthCode = "mockAuthCode";

        // loginService.getKaKaoAccessToken 메서드가 예외를 던지도록 설정합니다.
        when(loginService.getKaKaoAccessToken(anyString())).thenThrow(new RuntimeException("사용자 저장에 실패했습니다."));

        // When
        ResponseEntity<BaseResponse<UserKakaoLoginResponseDto>> responseEntity = loginController.kakaoCallback(mockAuthCode);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        BaseResponse<UserKakaoLoginResponseDto> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.getStatusCode());
        assertEquals("카카오 로그인 처리 중 오류 발생", responseBody.getMessage());
        assertNull(responseBody.getData());

        // 예외가 발생했을 때 loginService.kakaoLogin 및 jwtTokenProvider.createToken 메서드가 호출되지 않았음을 검증합니다.
        verify(loginService, never()).kakaoLogin(anyString());
        verify(jwtTokenProvider, never()).createToken(anyString());
    }

    /**
     * KakaoCallback 메서드에서 BaseException 예외가 발생할 경우의 처리를 테스트합니다.
     *
     * @throws Exception 예외 발생 시 처리됩니다.
     */
    @Test
    public void testKakaoCallback_BaseExceptionHandling() throws Exception {
        // Given
        String mockAuthCode = "mockAuthCode";

        // loginService.getKaKaoAccessToken 메서드가 BaseException을 던지도록 설정합니다.
        when(loginService.getKaKaoAccessToken(anyString())).thenThrow(new LoginService.BaseException(LoginService.BaseResponseCode.FAILED_TO_SAVE_USER));

        // When
        ResponseEntity<BaseResponse<UserKakaoLoginResponseDto>> responseEntity = loginController.kakaoCallback(mockAuthCode);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        BaseResponse<UserKakaoLoginResponseDto> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.getStatusCode());
        assertEquals("카카오 로그인 처리 중 오류 발생", responseBody.getMessage());
        assertNull(responseBody.getData());

        // 예외가 발생했을 때 loginService.kakaoLogin 및 jwtTokenProvider.createToken 메서드가 호출되지 않았음을 검증합니다.
        verify(loginService, never()).kakaoLogin(anyString());
        verify(jwtTokenProvider, never()).createToken(anyString());
    }
}
