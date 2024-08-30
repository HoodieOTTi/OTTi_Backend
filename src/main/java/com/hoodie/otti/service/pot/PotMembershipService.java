package com.hoodie.otti.service.pot;

import com.hoodie.otti.dto.pot.PotMembershipDTO;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.PotMembershipRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
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

        PotMembership membership = potMembershipRepository.findByUserAndPot(user, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
        membership.setApproved(false);
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

    // 권한을 갖고있는 사용자가 특정 유저의 권한 제거
    public void removeUserFromPotByCreator(Principal principal, Long requesterId, Pot pot) {
        Long userId = Long.parseLong(principal.getName());

        // 현재 사용자(principal)를 데이터베이스에서 조회
        User currentUser = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("removeUserFromPot : 현재 사용자를 찾을 수 없습니다."));

        // 현재 사용자가 해당 Pot에 권한이 있는지 확인
        boolean hasPermission = potMembershipRepository.existsByUserAndPotAndHasPermission(currentUser, pot, true);
        if (!hasPermission) {
            throw new SecurityException("removeUserFromPot : 권한이 없습니다.");
        }

        // 권한을 제거할 사용자를 조회
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("removeUserFromPot : 권한을 제거할 사용자를 찾을 수 없습니다."));

        // 해당 Pot에 대한 사용자의 PotMembership을 조회
        PotMembership membership = potMembershipRepository.findByUserAndPot(requester, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));

        // 승인 상태를 false로 설정
        membership.setApproved(false);
        potMembershipRepository.save(membership);
    }

    // potId에 해당하는 승인된 멤버 목록을 조회
    public List<PotMembershipDTO> getApprovedMembersByPotId(Long potId) {
        // potId에 해당하며 승인된 멤버를 조회
        List<PotMembership> potMemberships = potMembershipRepository.findByPotIdAndApproved(potId, true);

        // 결과를 담을 리스트 생성
        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        // 각 PotMembership을 PotMembershipDTO로 변환하여 리스트에 추가
        for (PotMembership membership : potMemberships) {
            User user = membership.getUser();
            Pot pot = membership.getPot();

            membershipDTOs.add(new PotMembershipDTO(
                    membership.getId(),
                    pot.getId(),
                    pot.getName(),
                    user.getId(),
                    user.getUsername(),
                    membership.getApproved(),
                    membership.hasPermission()
            ));
        }

        return membershipDTOs; // 최종 DTO 리스트 반환
    }

    // 현재 유저의 승인된 pot 목록을 조회
    public List<PotMembershipDTO> getApprovedPotsByUserId(Long userId) {
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("getApprovedPotsByUserId : 사용자를 찾을 수 없습니다."));

        // user와 승인된 상태(true)인 pot 목록을 조회
        List<PotMembership> potMemberships = potMembershipRepository.findByUserAndApproved(user, true);

        // 결과를 담을 리스트 생성
        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        // 각 PotMembership을 PotMembershipDTO로 변환하여 리스트에 추가
        for (PotMembership membership : potMemberships) {
            Pot pot = membership.getPot();

            membershipDTOs.add(new PotMembershipDTO(
                    membership.getId(),
                    pot.getId(),
                    pot.getName(),
                    user.getId(),
                    user.getUsername(),
                    membership.getApproved(),
                    membership.hasPermission()
            ));
        }

        return membershipDTOs; // 최종 DTO 리스트 반환
    }

    // 현재 유저의 권한을 가진 pot 목록을 조회
    public List<PotMembershipDTO> hasPermissionPotsByUserId(Long userId) {
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("getApprovedPotsByUserId : 사용자를 찾을 수 없습니다."));

        // user와 승인된 상태(true)인 pot 목록을 조회
        List<PotMembership> potMemberships = potMembershipRepository.findByUserAndHasPermission(user, true);

        // 결과를 담을 리스트 생성
        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        // 각 PotMembership을 PotMembershipDTO로 변환하여 리스트에 추가
        for (PotMembership membership : potMemberships) {
            Pot pot = membership.getPot();

            membershipDTOs.add(new PotMembershipDTO(
                    membership.getId(),
                    pot.getId(),
                    pot.getName(),
                    user.getId(),
                    user.getUsername(),
                    membership.getApproved(),
                    membership.hasPermission()
            ));
        }

        return membershipDTOs; // 최종 DTO 리스트 반환
    }

}
