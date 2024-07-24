package com.hoodie.otti.service.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.profile.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userProfileRepository) {
        this.userRepository = userProfileRepository;
    }

    /**
     * 유저 ID로 프로필 조회
     * @param id 유저 ID
     * @return 조회된 UserProfile 객체
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public User getUserProfileById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("ID에 해당하는 유저 프로필을 찾을 수 없습니다: " + id));
    }

    /**
     * 유저 프로필 전체 업데이트
     * @param userId 유저 ID
     * @param userProfileDTO UserProfileDTO 객체
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public void updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
        User userProfile = getUserProfileById(userId);
        userProfile.setUsername(userProfileDTO.getUsername());
        userProfile.setProfilePhotoUrl(userProfileDTO.getProfilePhotoUrl());
        userRepository.save(userProfile);
    }

    /**
     * 새로운 유저 프로필 저장
     * @param userProfile 저장할 UserProfile 객체
     * @return 저장된 UserProfile 객체
     */
    public User saveUserProfile(User userProfile) {
        return userRepository.save(userProfile);
    }

    /**
     * 유저 프로필 삭제
     * @param id 유저 ID
     */
    public void deleteUserProfile(Long id) {
        userRepository.deleteById(id);
    }
}
