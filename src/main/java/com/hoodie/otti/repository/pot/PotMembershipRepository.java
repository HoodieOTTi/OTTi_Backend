package com.hoodie.otti.repository.pot;

import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import com.hoodie.otti.model.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotMembershipRepository extends JpaRepository<PotMembership, Long> {
    // 특정 'pot'과 사용자에 대한 권한 상태를 확인
    boolean existsByUserAndPotAndHasPermission(User user, Pot pot, boolean hasPermission);

    // 특정 'pot'과 사용자에 대한 PotMembership을 조회
    Optional<PotMembership> findByUserAndPot(User user, Pot pot);

    // potId에 해당하며 승인된 멤버를 조회
    List<PotMembership> findByPotIdAndApproved(Long potId, boolean approved);

    // userId에 해당하며 승인된 pot 목록을 조회
    List<PotMembership> findByUserIdAndApproved(Long userId, boolean approved);
}
