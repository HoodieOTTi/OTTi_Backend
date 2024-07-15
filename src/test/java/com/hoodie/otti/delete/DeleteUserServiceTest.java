package com.hoodie.otti.delete;

import com.hoodie.otti.entity.login.User;
import com.hoodie.otti.exception.delete.DeleteUserException;
import com.hoodie.otti.repository.delete.DeleteUserRepository;
import com.hoodie.otti.service.delete.DeleteUserService;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteUserServiceTest {

    @Mock
    private DeleteUserRepository userRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private DeleteUserService userService;

    /**
     * 각 테스트 메서드 실행 전 MockitoAnnotations를 초기화합니다.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 유효한 토큰으로 사용자 삭제가 성공적으로 수행되는지 테스트합니다.
     */
    @Test
    public void testDeleteUser_Success() {
        // 토큰 유효성 검사를 모의 설정합니다.
        when(tokenProvider.validateToken(anyString())).thenReturn(true);
        when(tokenProvider.getUserEmailFromToken(anyString())).thenReturn("test@example.com");

        // 리포지토리 동작을 모의 설정합니다.
        User user = new User();
        user.setUserEmail("test@example.com");
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);

        // 삭제 메서드를 모의 설정합니다.
        doNothing().when(userRepository).delete(any(User.class));

        // 토큰 무효화를 모의 설정합니다.
        doNothing().when(tokenProvider).invalidateToken(anyString());

        // 서비스 메서드를 호출하고 예외가 발생하지 않는지 확인합니다.
        assertDoesNotThrow(() -> userService.deleteUser("test@example.com", "mockedToken"));
    }

    /**
     * 유효하지 않은 토큰으로 deleteUser 메서드가 DeleteUserException을 던지는지 테스트합니다.
     */
    @Test
    public void testDeleteUser_InvalidToken() {
        // 토큰 유효성 검사 실패를 모의 설정합니다.
        when(tokenProvider.validateToken(anyString())).thenReturn(false);

        // deleteUser 메서드가 "Invalid token" 메시지와 함께 DeleteUserException을 던지는지 확인합니다.
        DeleteUserException exception = assertThrows(DeleteUserException.class,
                () -> userService.deleteUser("test@example.com", "invalidToken"));
        assertEquals("Invalid token", exception.getMessage());
    }
}
