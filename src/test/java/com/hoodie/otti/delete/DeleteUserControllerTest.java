package com.hoodie.otti.delete;

import com.hoodie.otti.controller.delete.DeleteUserController;
import com.hoodie.otti.exception.delete.DeleteUserException;
import com.hoodie.otti.service.delete.DeleteUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteUserControllerTest {

    @Mock
    private DeleteUserService deleteUserService;

    @InjectMocks
    private DeleteUserController deleteUserController;

    /**
     * 각 테스트 메서드 실행 전 MockitoAnnotations를 초기화합니다.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * deleteUser 메서드가 성공적으로 사용자를 삭제하는지 테스트합니다.
     */
    @Test
    public void testDeleteUser_Success() {
        // deleteUserService의 deleteUser 메서드가 true를 반환하도록 설정
        when(deleteUserService.deleteUser(anyString(), anyString())).thenReturn(true);

        // 컨트롤러 메서드를 호출하여 응답을 테스트합니다.
        ResponseEntity<String> response = deleteUserController.deleteUser("test@example.com", "mockedToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account successfully deleted.", response.getBody());
    }

    /**
     * deleteUser 메서드가 사용자 삭제에 실패한 경우를 테스트합니다.
     */
    @Test
    public void testDeleteUser_Failure() {
        // deleteUserService의 deleteUser 메서드가 false를 반환하도록 설정
        when(deleteUserService.deleteUser(anyString(), anyString())).thenReturn(false);

        // 컨트롤러 메서드를 호출하여 응답을 테스트합니다.
        ResponseEntity<String> response = deleteUserController.deleteUser("test@example.com", "mockedToken");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete account.", response.getBody());
    }

    /**
     * deleteUser 메서드에서 DeleteUserException이 발생하는 경우를 테스트합니다.
     */
    @Test
    public void testDeleteUser_ExceptionHandling() {
        // deleteUserService의 deleteUser 메서드가 DeleteUserException을 던지도록 설정
        doThrow(new DeleteUserException("User not found")).when(deleteUserService).deleteUser(anyString(), anyString());

        // 컨트롤러 메서드를 호출하여 응답을 테스트합니다.
        ResponseEntity<String> response = deleteUserController.deleteUser("test@example.com", "mockedToken");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

}
