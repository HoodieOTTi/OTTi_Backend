package com.hoodie.otti.repository.pot;

import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotMembershipRepository extends JpaRepository<PotMembership, Long> {

    boolean existsByUserAndPotAndHasPermission(User user, Pot pot, boolean hasPermission);

    Optional<PotMembership> findByUserAndPot(User user, Pot pot);

    List<PotMembership> findByPotIdAndApproved(Long potId, boolean approved);

    List<PotMembership> findByUserAndApproved(User user, boolean approved);

    List<PotMembership> findByUserAndHasPermission(User user, boolean hasPermission);

    Optional<Pot> findPotById(Long potId);



}


