package com.hoodie.otti.service.pot;

import com.hoodie.otti.dto.pot.JoinRequestDTO;
import com.hoodie.otti.dto.pot.JoinRequestDescriptionDTO;
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

    public void createJoinRequest(Principal principal, Pot pot, JoinRequestDescriptionDTO joinRequestDescriptionDTO) {
        Long userId = Long.parseLong(principal.getName());

        User requester = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 이름이 조회되지 않습니다."));

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setRequester(requester);
        joinRequest.setPot(pot);
        joinRequest.setJoinrequestDescription(joinRequestDescriptionDTO.getJoinrequestDescription());
        joinRequest.setApproved(null);

        joinRequestRepository.save(joinRequest);

        notificationService.sendPotJoinRequestNotification(pot, joinRequest);
    }


    public void handleJoinRequest(Principal principal, Pot pot, boolean approve) {

        User requester = pot.getJoinRequests().stream()
                .filter(joinRequest -> joinRequest.getPot().equals(pot))
                .findFirst()
                .map(JoinRequest::getRequester)
                .orElseThrow(() -> new EntityNotFoundException("handleJoinRequest : Pot에서 가입 신청을 한 사용자를 찾을 수 없습니다."));

        JoinRequest joinRequest = joinRequestRepository.findByRequesterAndPot(requester, pot)
                .orElseThrow(() -> new EntityNotFoundException("handleJoinRequest : 가입 신청(JoinRequest)이 조회되지 않습니다."));

        if (!potMembershipService.userHasPermission(principal, pot)) {
            throw new SecurityException("handleJoinRequest : 권한을 가진 사용자만 승인과 거절을 선택할 수 있습니다.");
        }

        if (approve) {
            joinRequest.setApproved(true);
            potMembershipService.addUserToPot(requester.getKakaoId(), pot);
            notificationService.sendJoinApprovalNotification(requester, pot);
        } else {
            joinRequest.setApproved(false);
            notificationService.sendJoinRejectionNotification(requester, pot);
        }

        joinRequestRepository.save(joinRequest);

        System.out.println("joinRequest 상태 저장 완료: approved = " + joinRequest.isApproved());
    }


    public List<JoinRequestDTO> getJoinRequestsByPot(Long potId) {
        Pot pot = potRepository.findById(potId)
                .orElseThrow(() -> new EntityNotFoundException("getJoinRequestsByPot : PotId가 없습니다."));
        return joinRequestRepository.findByPot(pot).stream()
                .map(JoinRequestDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public List<JoinRequestDTO> getJoinRequestsByUser(Long userId) {
        User user = userProfileService.getUserProfileById(userId);
        List<JoinRequest> joinRequests = joinRequestRepository.findByRequester(user);

        return joinRequests.stream()
                .map(JoinRequestDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<JoinRequestDTO> getJoinRequestsByPrincipal(Principal principal) {
        User user = userProfileService.getUserProfileByPrincipal(principal);

        List<JoinRequest> joinRequests = joinRequestRepository.findByRequester(user);

        return joinRequests.stream()
                .map(JoinRequestDTO::fromEntity)
                .collect(Collectors.toList());
    }


}



