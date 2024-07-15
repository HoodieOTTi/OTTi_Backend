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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser_Success() {
        // Mocking service method return value
        when(deleteUserService.deleteUser(anyString(), anyString())).thenReturn(true);

        // Test controller method
        ResponseEntity<String> response = deleteUserController.deleteUser("test@example.com", "mockedToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account successfully deleted.", response.getBody());
    }

    @Test
    public void testDeleteUser_Failure() {
        // Mocking service method return value
        when(deleteUserService.deleteUser(anyString(), anyString())).thenReturn(false);

        // Test controller method
        ResponseEntity<String> response = deleteUserController.deleteUser("test@example.com", "mockedToken");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete account.", response.getBody());
    }

    @Test
    public void testDeleteUser_ExceptionHandling() {
        // Mocking service method to throw an exception
        doThrow(new DeleteUserException("User not found")).when(deleteUserService).deleteUser(anyString(), anyString());

        // Test controller method
        ResponseEntity<String> response = deleteUserController.deleteUser("test@example.com", "mockedToken");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    // Add more tests for other scenarios like invalid token, email mismatch, etc.
}
