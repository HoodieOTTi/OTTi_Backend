package com.hoodie.otti.service.pot;

import com.hoodie.otti.dto.pot.JoinRequestDTO;
import com.hoodie.otti.dto.pot.PotMembershipDTO;
import com.hoodie.otti.dto.pot.PotMembershipUserDTO;
import com.hoodie.otti.model.pot.JoinRequest;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.JoinRequestRepository;
import com.hoodie.otti.repository.pot.PotMembershipRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PotMembershipService {

    @Autowired
    private PotMembershipRepository potMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JoinRequestRepository joinRequestRepository;

    public void addUserToPot(Long userId, Pot pot) {
        PotMembership membership = new PotMembership();

        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("addUserToPot : 사용자를 찾을 수 없습니다."));

        membership.setUser(user);
        membership.setPot(pot);
        membership.setApproved(true);
        potMembershipRepository.save(membership);
    }

    public boolean userHasPermission(Principal principal, Pot pot) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("userHasPermission : 사용자를 찾을 수 없습니다."));
        return potMembershipRepository.existsByUserAndPotAndHasPermission(user, pot, true);
    }

    public List<Pot> getPotsWithPermissionByUserId(Long userId) {
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<PotMembership> memberships = potMembershipRepository.findByUserAndHasPermission(user, true);

        return memberships.stream()
                .map(PotMembership::getPot)
                .collect(Collectors.toList());
    }


    public boolean requesterHasPermission(User user, Pot pot) {
        Long userId = user.getId();
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("requesterHasPermission : 사용자를 찾을 수 없습니다."));

        return potMembershipRepository.existsByUserAndPotAndHasPermission(requester, pot, true);
    }


    public void removeUserFromPot(Principal principal, Pot pot) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("removeUserFromPot : 사용자를 찾을 수 없습니다."));

        PotMembership membership = potMembershipRepository.findByUserAndPot(user, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
        membership.setApproved(false);
        potMembershipRepository.save(membership);
    }

    public PotMembership getMembership(Principal principal, Pot pot) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("getMembership : 사용자를 찾을 수 없습니다."));

        return potMembershipRepository.findByUserAndPot(user, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));
    }

    public void removeUserFromPotByCreator(Principal principal, Long requesterId, Pot pot) {
        Long userId = Long.parseLong(principal.getName());

        User currentUser = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("removeUserFromPot : 현재 사용자를 찾을 수 없습니다."));

        boolean hasPermission = potMembershipRepository.existsByUserAndPotAndHasPermission(currentUser, pot, true);
        if (!hasPermission) {
            throw new SecurityException("removeUserFromPot : 권한이 없습니다.");
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("removeUserFromPot : 권한을 제거할 사용자를 찾을 수 없습니다."));

        PotMembership membership = potMembershipRepository.findByUserAndPot(requester, pot)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));

        membership.setApproved(false);
        potMembershipRepository.save(membership);
    }

    public List<PotMembershipDTO> getApprovedMembersByPotId(Long potId) {

        List<PotMembership> potMemberships = potMembershipRepository.findByPotIdAndApproved(potId, true);
        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        for (PotMembership membership : potMemberships) {
            User user = membership.getUser();
            Pot pot = membership.getPot();

            PotMembershipUserDTO membershipUserDTO = PotMembershipUserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .profilePhotoUrl(user.getProfilePhotoUrl())
                    .build();

            membershipDTOs.add(PotMembershipDTO.builder()
                    .id(membership.getId())
                    .potId(pot.getId())
                    .potName(pot.getName())
                    .user(membershipUserDTO)
                    .approved(membership.getApproved())
                    .hasPermission(membership.hasPermission())
                    .build());
        }

        return membershipDTOs;
    }

    public List<PotMembershipDTO> getApprovedPotsByUserId(Long userId) {
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("getApprovedPotsByUserId : 사용자를 찾을 수 없습니다."));

        List<PotMembership> potMemberships = potMembershipRepository.findByUserAndApproved(user, true);

        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        for (PotMembership membership : potMemberships) {
            Pot pot = membership.getPot();

            PotMembershipUserDTO membershipUserDTO = PotMembershipUserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .profilePhotoUrl(user.getProfilePhotoUrl())
                    .build();

            membershipDTOs.add(PotMembershipDTO.builder()
                    .id(membership.getId())
                    .potId(pot.getId())
                    .potName(pot.getName())
                    .user(membershipUserDTO)
                    .approved(membership.getApproved())
                    .hasPermission(membership.hasPermission())
                    .build());
        }

        return membershipDTOs;
    }

    public List<PotMembershipDTO> hasPermissionPotsByUserId(Long userId) {
        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("hasPermissionPotsByUserId : 사용자를 찾을 수 없습니다."));

        List<PotMembership> potMemberships = potMembershipRepository.findByUserAndHasPermission(user, true);

        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        for (PotMembership membership : potMemberships) {
            Pot pot = membership.getPot();

            PotMembershipUserDTO membershipUserDTO = PotMembershipUserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .profilePhotoUrl(user.getProfilePhotoUrl())
                    .build();

            membershipDTOs.add(PotMembershipDTO.builder()
                    .id(membership.getId())
                    .potId(pot.getId())
                    .potName(pot.getName())
                    .user(membershipUserDTO)
                    .approved(membership.getApproved())
                    .hasPermission(membership.hasPermission())
                    .build());
        }

        return membershipDTOs;
    }


    public List<JoinRequestDTO> getJoinRequestsForUserPots(Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<PotMembership> potMemberships = potMembershipRepository.findByUserAndHasPermission(user, true);

        List<JoinRequestDTO> joinRequestDTOs = new ArrayList<>();

        for (PotMembership membership : potMemberships) {
            Pot pot = membership.getPot();
            List<JoinRequest> joinRequests = joinRequestRepository.findByPot(pot);

            for (JoinRequest joinRequest : joinRequests) {
                JoinRequestDTO joinRequestDTO = JoinRequestDTO.fromEntity(joinRequest);
                joinRequestDTOs.add(joinRequestDTO);
            }
        }

        return joinRequestDTOs;
    }

    public List<PotMembershipDTO> getApproveOrPermissionJoinRequestsByUser(Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<PotMembership> potMemberships = potMembershipRepository.findByUserAndApprovedOrUserAndHasPermission(user, true, true);

        List<PotMembershipDTO> membershipDTOs = new ArrayList<>();

        for (PotMembership membership : potMemberships) {
            Pot pot = membership.getPot();

            PotMembershipUserDTO membershipUserDTO = PotMembershipUserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .profilePhotoUrl(user.getProfilePhotoUrl())
                    .build();

            membershipDTOs.add(PotMembershipDTO.builder()
                    .id(membership.getId())
                    .potId(pot.getId())
                    .potName(pot.getName())
                    .user(membershipUserDTO)
                    .approved(membership.getApproved())
                    .hasPermission(membership.hasPermission())
                    .build());
        }

        return membershipDTOs;
    }

}
