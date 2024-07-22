package com.hoodie.otti.profile;

import com.hoodie.otti.controller.profile.UserProfileController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.service.profile.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController profileController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testUpdateProfile_Success() throws Exception {
        // Given
        Long userId = 1L;
        UserProfileDTO dto = new UserProfileDTO("John Doe", "http://example.com/photo.jpg");

        // When
        doNothing().when(userProfileService).updateUserProfile(eq(userId), any(UserProfileDTO.class));

        // Then
        mockMvc.perform(put("/api/users/{userId}/profile/update", userId)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userProfileService).updateUserProfile(eq(userId), any(UserProfileDTO.class));
    }

    @Test
    public void testUpdateProfile_UserNotFound() throws Exception {
        // Given
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO("newUsername", "http://example.com/photo.jpg");

        doThrow(new UserProfileNotFoundException("User profile not found"))
                .when(userProfileService).updateUserProfile(eq(userId), any(UserProfileDTO.class));

        // When
        mockMvc.perform(put("/api/users/{userId}/profile/update", userId)
                        .content(objectMapper.writeValueAsString(userProfileDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
