package com.hoodie.otti.delete;

import com.hoodie.otti.controller.delete.DeleteUserController;
import com.hoodie.otti.service.delete.DeleteUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class DeleteUserControllerTest {

    @Mock
    private DeleteUserService deleteUserService;

    @InjectMocks
    private DeleteUserController deleteUserController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(deleteUserController).build();
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        String userEmail = "test@example.com";
        String token = "valid.jwt.token";

        when(deleteUserService.deleteUser(userEmail, token)).thenReturn(true);

        mockMvc.perform(delete("/api/delete-user")
                        .param("userEmail", userEmail)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Account successfully deleted."));
    }

    @Test
    public void testDeleteUserFailure() throws Exception {
        String userEmail = "test@example.com";
        String token = "invalid.jwt.token";

        when(deleteUserService.deleteUser(userEmail, token)).thenReturn(false);

        mockMvc.perform(delete("/api/delete-user")
                        .param("userEmail", userEmail)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to delete account."));
    }
}

