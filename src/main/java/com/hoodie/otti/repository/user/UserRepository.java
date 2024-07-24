package com.hoodie.otti.repository.user;

import com.hoodie.otti.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long UserId);

    Optional<User> findByUserEmail(String userEmail);
}
