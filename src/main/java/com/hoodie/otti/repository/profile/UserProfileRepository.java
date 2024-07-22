package com.hoodie.otti.repository.profile;

import com.hoodie.otti.model.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
