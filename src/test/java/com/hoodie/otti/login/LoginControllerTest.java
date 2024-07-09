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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testKakaoCallback_Success() throws Exception {
        // Given
        String mockAccessToken = "mockAccessToken";
        String mockEmail = "test@example.com";
        UserKakaoLoginResponseDto mockResponseDto = new UserKakaoLoginResponseDto(HttpStatus.OK, "mockToken", mockEmail);

        when(loginService.getKaKaoAccessToken(anyString())).thenReturn(mockAccessToken);
        when(loginService.kakaoLogin(mockAccessToken)).thenReturn(mockResponseDto);
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

        // Verify that jwtTokenProvider.createToken was called with the correct argument
        verify(jwtTokenProvider, times(1)).createToken(mockEmail);
    }

    @Test
    public void testKakaoCallback_ExceptionHandling() throws Exception {
        // Given
        String mockAuthCode = "mockAuthCode";

        // 예외 처리 방식 변경: RuntimeException으로 예외 발생
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

        // 예외가 발생했을 때는 다음 메서드들이 호출되지 않았음을 검증
        verify(loginService, never()).kakaoLogin(anyString());
        verify(jwtTokenProvider, never()).createToken(anyString());
    }

    @Test
    public void testKakaoCallback_BaseExceptionHandling() throws Exception {
        // Given
        String mockAuthCode = "mockAuthCode";

        // 예외 처리 방식 변경: BaseException으로 예외 발생
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

        // 예외가 발생했을 때는 다음 메서드들이 호출되지 않았음을 검증
        verify(loginService, never()).kakaoLogin(anyString());
        verify(jwtTokenProvider, never()).createToken(anyString());
    }
}
