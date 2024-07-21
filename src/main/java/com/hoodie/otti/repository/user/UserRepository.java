package com.hoodie.otti.repository.user;

import com.hoodie.otti.entity.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoUserId(String kakaoUserId);

    void deleteByUserEmail(String userEmail);

    void deleteByKakaoUserId(String kakaoUserId);
}
