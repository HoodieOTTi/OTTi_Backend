package com.hoodie.otti.user;

import com.hoodie.otti.controller.user.KakaoOAuth2Controller;
import com.hoodie.otti.dto.login.KakaoTokenResponse;
import com.hoodie.otti.dto.login.KakaoProfileResponse;
import com.hoodie.otti.entity.login.User;
import com.hoodie.otti.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class KakaoOAuth2ControllerTest {

    @InjectMocks
    private KakaoOAuth2Controller kakaoOAuth2Controller;

    @Mock
    private UserService userService;

    @Mock
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(kakaoOAuth2Controller).build();
    }

    @Test
    void testKakaoLogin() throws Exception {
        mockMvc.perform(get("/login/kakaologin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("https://kauth.kakao.com/oauth/authorize*"));
    }

    @Test
    void testKakaoCallback() throws Exception {
        // Mock KakaoTokenResponse
        KakaoTokenResponse mockTokenResponse = new KakaoTokenResponse();
        mockTokenResponse.setAccessToken("mock-access-token");

        // Mock KakaoProfileResponse
        KakaoProfileResponse mockProfileResponse = new KakaoProfileResponse();
        KakaoProfileResponse.KakaoAccount mockAccount = new KakaoProfileResponse.KakaoAccount();
        KakaoProfileResponse.KakaoAccount.Profile mockProfile = new KakaoProfileResponse.KakaoAccount.Profile();
        mockProfile.setNickname("mock-nickname");
        mockAccount.setProfile(mockProfile);
        mockAccount.setEmail("mock-email@example.com");
        mockProfileResponse.setId(12345L);
        mockProfileResponse.setKakaoAccount(mockAccount);

        // Mock the RestTemplate behavior for token request
        when(restTemplate.exchange(
                contains("https://kauth.kakao.com/oauth/token"),
                eq(HttpMethod.POST),
                argThat(request -> request.getHeaders().get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)),
                eq(KakaoTokenResponse.class)
        )).thenReturn(new ResponseEntity<>(mockTokenResponse, HttpStatus.OK));

        // Mock the RestTemplate behavior for profile request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer mock-access-token");
        headers.setContentType(MediaType.APPLICATION_JSON);

        when(restTemplate.exchange(
                contains("https://kapi.kakao.com/v2/user/me"),
                eq(HttpMethod.GET),
                argThat(request -> request.getHeaders().equals(headers)),
                eq(KakaoProfileResponse.class)
        )).thenReturn(new ResponseEntity<>(mockProfileResponse, HttpStatus.OK));

        // Mock UserService behavior
        when(userService.findByKakaoUserId(anyString())).thenReturn(java.util.Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(new User("mock-email@example.com", "12345", "mock-nickname"));

        mockMvc.perform(get("/oauth/kakao/callback")
                        .param("code", "mock-code"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("mock-email@example.com"))
                .andExpect(jsonPath("$.nickname").value("mock-nickname"));

        verify(userService, times(1)).saveUser(any(User.class));
    }



    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully logged out"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Mock the deleteUserByKakaoUserId method
        doNothing().when(userService).deleteUserByKakaoUserId(anyString());

        // Perform the deleteUser request
        MvcResult result = mockMvc.perform(post("/api/delete-user")
                        .param("kakaoUserId", "mock-kakao-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully deleted"))
                .andReturn();

        // Verify that deleteUserByKakaoUserId was called with the correct parameter
        verify(userService, times(1)).deleteUserByKakaoUserId("mock-kakao-id");
    }
}
