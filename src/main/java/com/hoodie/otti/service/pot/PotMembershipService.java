package com.hoodie.otti.service.pot;

import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.PotMembershipRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PotMembershipService {

    @Autowired
    private PotMembershipRepository potMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    // 사용자를 특정 'pot'에 추가하고 권한을 부여
    public void addUserToPot(Long userId, Pot pot) {
        PotMembership membership = new PotMembership();

        // 사용자 찾기
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("addUserToPot : 사용자를 찾을 수 없습니다."));

        // User 객체를 설정
        membership.setUser(user);
        membership.setPot(pot);
        membership.setApproved(true);
        potMembershipRepository.save(membership);
    }

    // 특정 'pot'에 대한 현재 계정 사용자의 권한 확인
    public boolean userHasPermission(Principal principal, Pot pot) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("userHasPermission : 사용자를 찾을 수 없습니다."));

        return potMembershipRepository.existsByUserAndPotAndHasPermission(user, pot, true);
    }

    // 특정 'pot'에 대한 requester의 권한 확인
    public boolean requesterHasPermission(User user, Pot pot) {
        Long userId = user.getId();
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("requesterHasPermission : 사용자를 찾을 수 없습니다."));

        return potMembershipRepository.existsByUserAndPotAndHasPermission(requester, pot, true);
    }


    // 사용자의 특정 'pot'에 대한 권한 제거
    public void removeUserFromPot(Principal principal, Pot pot) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("removeUserFromPot : 사용자를 찾을 수 없습니다."));

        PotMembership membership = potMembershipRepository.findByUserAndPot(user, pot) // 수정된 부분
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
        membership.setHasPermission(false);
        potMembershipRepository.save(membership);
    }

    // 사용자가 특정 'pot'에 대해 권한을 갖고 있는지 여부를 확인
    public PotMembership getMembership(Principal principal, Pot pot) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("getMembership : 사용자를 찾을 수 없습니다."));

        return potMembershipRepository.findByUserAndPot(user, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
    }

    // potId에 해당하는 승인된 멤버 목록을 조회
    public List<PotMembership> getApprovedMembersByPotId(Long potId) {
        return potMembershipRepository.findByPotIdAndApproved(potId, true);
    }

    // 특정 userId에 해당하는 승인된 pot 목록을 조회
    public List<PotMembership> getApprovedPotsByUserId(Long userId) {
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("getApprovedPotsByUserId : 사용자를 찾을 수 없습니다."));

        // user와 승인된 상태(true)인 pot 목록을 조회
        return potMembershipRepository.findByUserAndApproved(user, true); // 수정된 부분
    }
}
