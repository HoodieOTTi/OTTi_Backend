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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser_Success() {
        // Mocking token validation
        when(tokenProvider.validateToken(anyString())).thenReturn(true);
        when(tokenProvider.getUserEmailFromToken(anyString())).thenReturn("test@example.com");

        // Mocking repository behavior
        User user = new User();
        user.setUserEmail("test@example.com");
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);

        // Mocking delete method
        doNothing().when(userRepository).delete(any(User.class));

        // Mocking token invalidation
        doNothing().when(tokenProvider).invalidateToken(anyString());

        // Test the service method
        assertDoesNotThrow(() -> userService.deleteUser("test@example.com", "mockedToken"));
    }

    @Test
    public void testDeleteUser_InvalidToken() {
        // Mocking token validation failure
        when(tokenProvider.validateToken(anyString())).thenReturn(false);

        // Test should throw DeleteUserException with "Invalid token" message
        DeleteUserException exception = assertThrows(DeleteUserException.class,
                () -> userService.deleteUser("test@example.com", "invalidToken"));
        assertEquals("Invalid token", exception.getMessage());
    }

    // Add more tests for other scenarios like email mismatch, user not found, unexpected errors, etc.
}
