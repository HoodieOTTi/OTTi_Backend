package com.hoodie.otti.service.profile;

import com.hoodie.otti.dto.community.ProfileImageResponseDto;
import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.profile.UserRepository;
import com.hoodie.otti.service.community.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    @Autowired
    public UserProfileService(UserRepository userProfileRepository,ImageService imageService) {
        this.userRepository = userProfileRepository;
        this.imageService = imageService;
    }


    public User getUserProfileById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("ID에 해당하는 유저 프로필을 찾을 수 없습니다: " + id));
    }

    public UserProfileDTO getUserProfileDTOByPrincipal(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("해당 토큰으로 유저 프로필을 찾을 수 없습니다: " + principal.getName()));

        return new UserProfileDTO(user.getUsername(), user.getProfilePhotoUrl());
    }

    public User getUserProfileByPrincipal(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        return userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("해당 토큰으로 유저 프로필을 찾을 수 없습니다: " + principal.getName()));
    }

    @Transactional
    public void updateUserProfile(Principal principal, UserProfileDTO userProfileDTO, UploadImageRequestDto requestDto) throws IOException {
        Optional<User> userOptional = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        User user = userOptional.get();

        String oldImageUrl = user.getProfilePhotoUrl();
        if (requestDto.getImage() != null) {
            try {
                imageService.deleteProfileImage(oldImageUrl);
            } catch (Exception e) {
                throw new RuntimeException("기존 이미지 삭제에 실패했습니다: " + e.getMessage());
            }
        }

        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            try {
                ProfileImageResponseDto responseDto = imageService.saveProfileImage(requestDto);
                user.setProfilePhotoUrl(responseDto.getImageUrl());
            } catch (IOException e) {
                throw new RuntimeException("새로운 이미지 저장에 실패했습니다: " + e.getMessage());
            }
        }

        if (userProfileDTO.getUsername() != null && !userProfileDTO.getUsername().equals(user.getUsername())) {
            user.setUsername(userProfileDTO.getUsername());
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("사용자 프로필 업데이트에 실패했습니다: " + e.getMessage());
        }

    }
}