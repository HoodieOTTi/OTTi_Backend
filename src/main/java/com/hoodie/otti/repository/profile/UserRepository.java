package com.hoodie.otti.repository.profile;

import com.hoodie.otti.model.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long UserId);

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByKakaoId(long kakaoId);

    Optional<User> findByToken(String token);
}
