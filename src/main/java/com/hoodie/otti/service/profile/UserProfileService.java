package com.hoodie.otti.service.profile;

import com.hoodie.otti.dto.community.ProfileImageResponseDto;
import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.PotRepository;
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
    private final PotRepository potRepository;
    private final ImageService imageService;

    @Autowired
    public UserProfileService(UserRepository userProfileRepository, PotRepository potRepository, ImageService imageService) {
        this.userRepository = userProfileRepository;
        this.potRepository = potRepository;
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

        // 기존 이미지가 있으면 삭제
        String oldImageUrl = user.getProfilePhotoUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty() && requestDto.getImage() != null) {
            // 새로운 이미지가 있을 경우에만 기존 이미지 삭제
            imageService.deleteProfileImage(oldImageUrl);
        }

        // 새로운 이미지 저장
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            ProfileImageResponseDto responseDto = imageService.saveProfileImage(requestDto);
            user.setProfilePhotoUrl(responseDto.getImageUrl());
        }

        // 사용자 이름 업데이트 (이름이 비어있지 않은 경우에만 업데이트)
        if (userProfileDTO.getUsername() != null && !userProfileDTO.getUsername().isEmpty()) {
            user.setUsername(userProfileDTO.getUsername());
        }

        // 변경된 사용자 정보 저장
        userRepository.save(user);
    }



    public User saveUserProfile(User userProfile) {
        return userRepository.save(userProfile);
    }

    public void deleteUserProfile(Long id) {
        userRepository.deleteById(id);
    }
}