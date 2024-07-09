package com.hoodie.otti.delete;

import com.hoodie.otti.repository.delete.DeleteUserRepository;
import com.hoodie.otti.service.delete.DeleteUserService;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class DeleteUserServiceTest {

    @Mock
    private DeleteUserRepository deleteUserRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private DeleteUserService deleteUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUserSuccess() {
        String userEmail = "test@example.com";
        String token = "valid.jwt.token";

        doNothing().when(deleteUserRepository).deleteByEmail(userEmail);
        doNothing().when(jwtTokenProvider).invalidateToken(token);

        boolean result = deleteUserService.deleteUser(userEmail, token);
        assertTrue(result);
    }

    @Test
    public void testDeleteUserFailure() {
        String userEmail = "test@example.com";
        String token = "invalid.jwt.token";

        doThrow(new RuntimeException()).when(deleteUserRepository).deleteByEmail(userEmail);
        doThrow(new RuntimeException()).when(jwtTokenProvider).invalidateToken(token);

        boolean result = deleteUserService.deleteUser(userEmail, token);
        assertFalse(result);
    }
}
