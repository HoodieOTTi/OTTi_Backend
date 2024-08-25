package com.hoodie.otti.service.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.profile.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userProfileRepository) {
        this.userRepository = userProfileRepository;
    }


    public User getUserProfileById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("ID에 해당하는 유저 프로필을 찾을 수 없습니다: " + id));
    }

    public User getUserProfileByPrincipal(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        return userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("해당 토큰으로 유저 프로필을 찾을 수 없습니다: " + principal.getName()));
    }


//    public void updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
//        User userProfile = getUserProfileById(userId);
//        userProfile.setUsername(userProfileDTO.getUsername());
//        userProfile.setProfilePhotoUrl(userProfileDTO.getProfilePhotoUrl());
//        userRepository.save(userProfile);
//    }

    public void updateUserProfile(Principal principal, UserProfileDTO userProfileDTO) {
        User userProfile = getUserProfileByPrincipal(principal);
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