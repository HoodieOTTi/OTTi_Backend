package com.hoodie.otti.repository.login;

import com.hoodie.otti.entity.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoUserId(String kakaoUserId);
}
