package com.hoodie.otti.controller.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.service.profile.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = ProfileController.class)
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .build();
    }

    @Test
    public void testUpdateProfileUsername_Success() throws Exception {
        String newUsername = "newUsername";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/profile/username")
                        .param("newUsername", newUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(userProfileService, times(1)).updateUserProfileUsername(1L, newUsername);
    }

    @Test
    public void testUpdateProfile_ThrowUserProfileNotFoundException() throws Exception {
        UserProfileDTO userProfileDTO = new UserProfileDTO("newUsername", "newNickname", "http://example.com/photo.jpg");

        doThrow(new UserProfileNotFoundException("사용자 프로필을 찾을 수 없습니다")).when(userProfileService)
                .updateUserProfile(1L, userProfileDTO.getUsername(), userProfileDTO.getNickname(), userProfileDTO.getProfilePhotoUrl());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("사용자 프로필을 찾을 수 없습니다"))
                .andDo(MockMvcResultHandlers.print());
    }
}
