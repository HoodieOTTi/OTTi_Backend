package com.hoodie.otti.logout;

import com.hoodie.otti.controller.login.LoginController;
import com.hoodie.otti.controller.logout.LogoutController;
import com.hoodie.otti.service.logout.LogoutService;
import com.hoodie.otti.dto.login.BaseResponse;
import com.hoodie.otti.dto.login.UserKakaoLoginResponseDto;
import com.hoodie.otti.service.login.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutControllerTest {

    @Mock
    private LogoutService logoutService;

    @InjectMocks
    private LogoutController logoutController;

    @Test
    public void testLogout_Success() {
        // Given
        String mockToken = "mockToken";

        // When
        when(logoutService.logout(mockToken)).thenReturn(true);
        ResponseEntity<BaseResponse<String>> responseEntity = logoutController.logout(mockToken);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        BaseResponse<String> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("로그아웃 되었습니다.", responseBody.getMessage());

        // Verify that loginService.logout was called with the correct argument
        verify(logoutService, times(1)).logout(mockToken);
    }

    @Test
    public void testLogout_Failure() {
        // Given
        String mockToken = "mockToken";

        // When
        when(logoutService.logout(mockToken)).thenReturn(false);
        ResponseEntity<BaseResponse<String>> responseEntity = logoutController.logout(mockToken);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        BaseResponse<String> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.getStatusCode());
        assertEquals("로그아웃 처리 중 오류 발생", responseBody.getMessage());

        // Verify that loginService.logout was called with the correct argument
        verify(logoutService, times(1)).logout(mockToken);
    }
}
