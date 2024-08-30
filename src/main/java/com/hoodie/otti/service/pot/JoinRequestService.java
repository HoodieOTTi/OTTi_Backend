package com.hoodie.otti.service.pot;

import com.hoodie.otti.dto.pot.JoinRequestDTO;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.model.pot.JoinRequest;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.JoinRequestRepository;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import com.hoodie.otti.service.notification.NotificationService;
import com.hoodie.otti.service.profile.UserProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JoinRequestService {

    @Autowired
    private JoinRequestRepository joinRequestRepository;

    @Autowired
    private PotMembershipService potMembershipService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PotRepository potRepository;

    @Autowired
    private UserRepository userRepository;

    // 사용자가 특정 pot에 가입 신청을 생성
    public void createJoinRequest(Principal principal, Pot pot) {
        // Principal에서 사용자Id를 가져옴
        Long userId = Long.parseLong(principal.getName());

        System.out.println("Principal 사용자Id (=kakaoId) : " + principal.getName());

        // 사용자 이름을 통해 User 엔티티를 조회
        User requester = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 이름이 조회되지 않습니다."));

        // JoinRequest 생성
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setRequester(requester);
        joinRequest.setPot(pot);
        joinRequest.setApproved(null); // 대기 중으로 설정

        // JoinRequest 저장
        joinRequestRepository.save(joinRequest);
    }


    // 가입 신청을 승인하거나 거절
    public void handleJoinRequest(Principal principal, Pot pot, boolean approve) {
        Long userId = Long.parseLong(principal.getName());

        System.out.println("가입 신청을 한 사용자의 kakaoId(userId) : " + userId);

        // userId를 사용하여 User 객체를 조회
        User requester = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("가입 신청을 한 사용자를 찾을 수 없습니다."));

        // Principal을 사용하여 JoinRequest를 조회
        JoinRequest joinRequest = joinRequestRepository.findByRequesterAndPot(requester, pot)
                .orElseThrow(() -> new EntityNotFoundException("가입 신청(JoinRequest)이 조회되지 않습니다."));

        // 현재 사용자가 해당 Pot에 대한 권한을 가지고 있는지 확인
        if (!potMembershipService.userHasPermission(principal, pot)) {
            throw new SecurityException("권한을 가진 사용자만 승인과 거절을 선택할 수 있습니다.");
        }

        // 승인 또는 거절 처리
        if (approve) {
            potMembershipService.addUserToPot(requester.getKakaoId(), pot); // 요청자를 Pot에 추가
            notificationService.sendJoinApprovalNotification(requester, pot); // 승인 알림 전송
        } else {
            notificationService.sendJoinRejectionNotification(requester, pot); // 거절 알림 전송
        }
    }


    // 특정 pot에 대한 모든 가입 신청 목록을 조회
//    public List<JoinRequest> getJoinRequestsByPot(Long potId) {
//        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
//        return joinRequestRepository.findByPot(pot);
//    }
    // 특정 pot에 대한 모든 가입 신청 목록을 조회
    public List<JoinRequestDTO> getJoinRequestsByPot(Long potId) {
        Pot pot = potRepository.findById(potId)
                .orElseThrow(() -> new EntityNotFoundException("getJoinRequestsByPot : PotId가 없습니다."));
        // JoinRequest 엔티티를 JoinRequestDTO로 변환하여 반환
        return joinRequestRepository.findByPot(pot).stream()
                .map(JoinRequestDTO::fromEntity) // JoinRequest를 JoinRequestDTO로 변환
                .collect(Collectors.toList());
    }



    // 특정 유저가 제출한 모든 가입 신청 목록을 조회
    public List<JoinRequestDTO> getJoinRequestsByUser(Long userId) {
        User user = userProfileService.getUserProfileById(userId);
        List<JoinRequest> joinRequests = joinRequestRepository.findByRequester(user);

        // JoinRequest 엔티티 리스트를 DTO 리스트로 변환
        return joinRequests.stream()
                .map(joinRequest -> new JoinRequestDTO(
                        joinRequest.getId(),
                        joinRequest.getPot().getId(),
                        new UserProfileDTO(
                                joinRequest.getRequester().getUsername(),
                                joinRequest.getRequester().getProfilePhotoUrl()
                        ),
                        joinRequest.getApproved() // 승인 여부 추가
                ))
                .collect(Collectors.toList());
    }



}



