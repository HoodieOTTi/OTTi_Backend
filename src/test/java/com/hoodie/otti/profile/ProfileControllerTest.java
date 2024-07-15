package com.hoodie.otti.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.controller.profile.ProfileController;
import com.hoodie.otti.service.profile.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    /**
     * 각 테스트 메서드 실행 전 초기화 작업을 수행할 수 있도록 설정합니다.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .build();
    }

    /**
     * 프로필 사용자명 업데이트 성공 테스트입니다.
     *
     * @throws Exception 예외 발생 시 처리
     */
    @Test
    public void testUpdateProfileUsername_Success() throws Exception {
        // Given
        String newUsername = "newUsername";

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/profile/username")
                        .param("newUsername", newUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // Verify
        verify(userProfileService, times(1)).updateUserProfileUsername(1L, newUsername);
    }

    /**
     * 프로필 업데이트 시 UserProfileNotFoundException 예외를 처리하는 테스트입니다.
     *
     * @throws Exception 예외 발생 시 처리
     */
    @Test
    public void testUpdateProfile_ThrowUserProfileNotFoundException() throws Exception {
        // Given
        UserProfileDTO userProfileDTO = new UserProfileDTO("newUsername", "newNickname", "http://example.com/photo.jpg");

        // Mock 설정
        doThrow(new UserProfileNotFoundException("사용자 프로필을 찾을 수 없습니다")).when(userProfileService)
                .updateUserProfile(1L, userProfileDTO.getUsername(), userProfileDTO.getNickname(), userProfileDTO.getProfilePhotoUrl());

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("사용자 프로필을 찾을 수 없습니다"))
                .andDo(MockMvcResultHandlers.print());
    }
}
