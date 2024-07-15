package com.hoodie.otti.logout;

import com.hoodie.otti.controller.logout.LogoutController;
import com.hoodie.otti.service.logout.LogoutService;
import com.hoodie.otti.dto.login.BaseResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * LogoutControllerTest는 LogoutController 클래스의 로그아웃 기능을 테스트하는 단위 테스트 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
public class LogoutControllerTest {

    @Mock
    private LogoutService logoutService;

    @InjectMocks
    private LogoutController logoutController;

    /**
     * 로그아웃 성공 시나리오를 테스트합니다.
     */
    @Test
    public void testLogout_Success() {
        // Given
        String mockToken = "mockToken";

        // When
        when(logoutService.logout(mockToken)).thenReturn(true);
        ResponseEntity<BaseResponse<String>> responseEntity = logoutController.logout(mockToken);

        // Then
        // HTTP 상태코드가 OK인지 검증합니다.
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // 응답 바디의 상태코드와 메시지를 검증합니다.
        BaseResponse<String> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("로그아웃 되었습니다.", responseBody.getMessage());

        // logoutService.logout 메서드가 올바른 인자로 호출되었는지 검증합니다.
        verify(logoutService, times(1)).logout(mockToken);
    }

    /**
     * 로그아웃 실패 시나리오를 테스트합니다.
     */
    @Test
    public void testLogout_Failure() {
        // Given
        String mockToken = "mockToken";

        // When
        when(logoutService.logout(mockToken)).thenReturn(false);
        ResponseEntity<BaseResponse<String>> responseEntity = logoutController.logout(mockToken);

        // Then
        // HTTP 상태코드가 INTERNAL_SERVER_ERROR인지 검증합니다.
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        // 응답 바디의 상태코드와 메시지를 검증합니다.
        BaseResponse<String> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.getStatusCode());
        assertEquals("로그아웃 처리 중 오류 발생", responseBody.getMessage());

        // logoutService.logout 메서드가 올바른 인자로 호출되었는지 검증합니다.
        verify(logoutService, times(1)).logout(mockToken);
    }
}
