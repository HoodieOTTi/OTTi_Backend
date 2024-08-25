package com.hoodie.otti.service.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.profile.UserRepository;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserProfileService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User getUserProfileByToken(String token) {
        // JWT에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // 사용자 ID로 프로필 조회
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("해당 토큰으로 유저 프로필을 찾을 수 없습니다: " + token));
    }

    public User getUserProfileById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("ID에 해당하는 유저 프로필을 찾을 수 없습니다: " + id));
    }

    public void updateUserProfile(String token, UserProfileDTO userProfileDTO) {
        User userProfile = getUserProfileByToken(token);
        userProfile.setUsername(userProfileDTO.getUsername());
        userProfile.setProfilePhotoUrl(userProfileDTO.getProfilePhotoUrl());
        userRepository.save(userProfile);
    }

    public User saveUserProfile(User userProfile) {
        return userRepository.save(userProfile);
    }

    public void deleteUserProfile(Long id) {
        userRepository.deleteById(id);
    }
}
