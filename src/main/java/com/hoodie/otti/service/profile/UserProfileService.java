package com.hoodie.otti.service.profile;

import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.model.profile.UserProfile;
import com.hoodie.otti.repository.profile.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    /**
     * 유저 ID로 프로필 조회
     * @param id 유저 ID
     * @return 조회된 UserProfile 객체
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public UserProfile getUserProfileById(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("ID에 해당하는 유저 프로필을 찾을 수 없습니다: " + id));
    }

    /**
     * 프로필 사진 업데이트
     * @param userId 유저 ID
     * @param photoUrl 새로운 프로필 사진 URL
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public void updateUserProfilePhoto(Long userId, String photoUrl) {
        UserProfile userProfile = getUserProfileById(userId);
        userProfile.setProfilePhotoUrl(photoUrl);
        userProfileRepository.save(userProfile);
    }

    /**
     * 유저네임 업데이트
     * @param userId 유저 ID
     * @param newUsername 새로운 유저네임
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public void updateUserProfileUsername(Long userId, String newUsername) {
        UserProfile userProfile = getUserProfileById(userId);
        userProfile.setUsername(newUsername);
        userProfileRepository.save(userProfile);
    }

    /**
     * 닉네임 업데이트
     * @param userId 유저 ID
     * @param newNickname 새로운 닉네임
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public void updateUserProfileNickname(Long userId, String newNickname) {
        UserProfile userProfile = getUserProfileById(userId);
        userProfile.setNickname(newNickname);
        userProfileRepository.save(userProfile);
    }

    /**
     * 프로필 전체 업데이트
     * @param userId 유저 ID
     * @param newUsername 새로운 유저네임
     * @param newNickname 새로운 닉네임
     * @param photoUrl 새로운 프로필 사진 URL
     * @throws UserProfileNotFoundException 유저 프로필이 존재하지 않을 경우
     */
    public void updateUserProfile(Long userId, String newUsername, String newNickname, String photoUrl) {
        UserProfile userProfile = getUserProfileById(userId);
        userProfile.setUsername(newUsername);
        userProfile.setNickname(newNickname);
        userProfile.setProfilePhotoUrl(photoUrl);
        userProfileRepository.save(userProfile);
    }

    /**
     * 새로운 유저 프로필 저장
     * @param userProfile 저장할 UserProfile 객체
     * @return 저장된 UserProfile 객체
     */
    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    /**
     * 유저 프로필 삭제
     * @param id 유저 ID
     */
    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
}
