package com.hoodie.otti.repository.pot;

import com.hoodie.otti.model.pot.JoinRequest;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findByPot(Pot pot);
    List<JoinRequest> findByRequester(User requester);
}
