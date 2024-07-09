package com.hoodie.otti.repository.delete;

import com.hoodie.otti.model.profile.UserProfile;
import com.hoodie.otti.entity.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteUserRepository extends JpaRepository<User, Long> {
    void deleteByEmail(String email);
}
