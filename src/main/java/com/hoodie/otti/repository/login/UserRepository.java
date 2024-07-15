package com.hoodie.otti.repository.login;

import com.hoodie.otti.entity.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 정보를 관리하기 위한 JpaRepository 인터페이스
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 카카오 사용자 ID를 기준으로 사용자 정보를 조회하는 메서드
     *
     * @param kakaoUserId 카카오 사용자 ID
     * @return Optional<User> 사용자 엔티티 객체
     */
    Optional<User> findByKakaoUserId(String kakaoUserId);
}
