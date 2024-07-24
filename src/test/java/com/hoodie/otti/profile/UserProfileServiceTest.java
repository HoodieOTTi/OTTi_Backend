package com.hoodie.otti.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.repository.profile.UserRepository;
import com.hoodie.otti.service.profile.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserProfileServiceTest {

    @Mock
    private UserRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateProfile_Success() {
        // Given
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO("newUsername", "http://example.com/photo.jpg");

        User existingProfile = new User("oldUsername", "http://oldphoto.com/photo.jpg");
        existingProfile.setId(userId);

        when(userProfileRepository.findById(userId)).thenReturn(java.util.Optional.of(existingProfile));
        when(userProfileRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        userProfileService.updateUserProfile(userId, userProfileDTO);

        // Verify interactions
        verify(userProfileRepository).findById(userId);
        verify(userProfileRepository).save(any(User.class));
    }

    @Test
    public void testUpdateProfile_UserNotFound() {
        // Given
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO("newUsername", "http://example.com/photo.jpg");

        when(userProfileRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(UserProfileNotFoundException.class, () -> {
            userProfileService.updateUserProfile(userId, userProfileDTO);
        });
    }
}
