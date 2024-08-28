package com.hoodie.otti.service.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final PotRepository potRepository;

    @Autowired
    public UserProfileService(UserRepository userProfileRepository, PotRepository potRepository) {
        this.userRepository = userProfileRepository;
        this.potRepository = potRepository;
    }


    public User getUserProfileById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("ID에 해당하는 유저 프로필을 찾을 수 없습니다: " + id));
    }

    public UserProfileDTO getUserProfileByPrincipal(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("해당 토큰으로 유저 프로필을 찾을 수 없습니다: " + principal.getName()));

        return new UserProfileDTO(user.getUsername(), user.getProfilePhotoUrl());
    }


//    public void updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
//        User userProfile = getUserProfileById(userId);
//        userProfile.setUsername(userProfileDTO.getUsername());
//        userProfile.setProfilePhotoUrl(userProfileDTO.getProfilePhotoUrl());
//        userRepository.save(userProfile);
//    }

    public void updateUserProfile(Principal principal, UserProfileDTO userProfileDTO) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        user.get().setUsername(userProfileDTO.getUsername());
        user.get().setProfilePhotoUrl(userProfileDTO.getProfilePhotoUrl());
        userRepository.save(user.get());
    }


    public User saveUserProfile(User userProfile) {
        return userRepository.save(userProfile);
    }

    public void deleteUserProfile(Long id) {
        userRepository.deleteById(id);
    }
}