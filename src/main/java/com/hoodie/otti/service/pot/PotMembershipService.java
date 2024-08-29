package com.hoodie.otti.service.pot;

import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.PotMembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotMembershipService {

    @Autowired
    private PotMembershipRepository potMembershipRepository;

    // 사용자를 특정 'pot'에 추가하고 권한을 부여
    public void addUserToPot(User user, Pot pot) {
        PotMembership membership = new PotMembership();
        membership.setUser(user);
        membership.setPot(pot);
        membership.setHasPermission(true);
        potMembershipRepository.save(membership);
    }

    // 특정 'pot'에 대한 사용자의 권한 확인
    public boolean userHasPermission(User user, Pot pot) {
        return potMembershipRepository.existsByUserAndPotAndHasPermission(user, pot, true);
    }

    // 사용자의 특정 'pot'에 대한 권한 제거
    public void removeUserFromPot(User user, Pot pot) {
        PotMembership membership = potMembershipRepository.findByUserAndPot(user, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
        membership.setHasPermission(false);
        potMembershipRepository.save(membership);
    }

    // 사용자가 특정 'pot'에 대해 권한을 갖고 있는지 여부를 확인
    public PotMembership getMembership(User user, Pot pot) {
        return potMembershipRepository.findByUserAndPot(user, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
    }

    // potId에 해당하는 승인된 멤버 목록을 조회
    public List<PotMembership> getApprovedMembersByPotId(Long potId) {
        return potMembershipRepository.findByPotIdAndApproved(potId, true);
    }

    // 특정 userId에 해당하는 승인된 pot 목록을 조회
    public List<PotMembership> getApprovedPotsByUserId(Long userId) {
        // userId와 승인된 상태(true)인 pot 목록을 조회
        return potMembershipRepository.findByUserIdAndApproved(userId, true);
    }
}
