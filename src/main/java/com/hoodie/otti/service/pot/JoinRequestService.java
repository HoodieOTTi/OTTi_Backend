package com.hoodie.otti.service.pot;

import com.hoodie.otti.model.pot.JoinRequest;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.pot.JoinRequestRepository;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.service.notification.NotificationService;
import com.hoodie.otti.service.profile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 사용자가 특정 pot에 가입 신청을 생성
    public void createJoinRequest(User requester, Pot pot) {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setRequester(requester);
        joinRequest.setPot(pot);
        joinRequest.setApproved(null); // 대기 상태
        joinRequestRepository.save(joinRequest);

        // 권한이 있는 user에게 알림 보내기
        notificationService.sendPotJoinRequestNotification(pot, joinRequest);
    }

    // 가입 신청을 승인하거나 거절
//    public void handleJoinRequest(Long joinRequestId, Long adminId, boolean approve) {
//        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId)
//                .orElseThrow(() -> new EntityNotFoundException("Join Request not found"));
//
//        Pot pot = joinRequest.getPot();
//        Pot pot = joinRequest.getPot();
//        User admin = userProfileService.getUserProfileById(adminId);
//
//        if (!pot.getCreatorId().equals(admin)) {
//            throw new SecurityException("권한을 가진 사용자만 승인과 거절을 선택할 수 있습니다.");
//        }
//
//        joinRequest.setApproved(approve);
//        joinRequestRepository.save(joinRequest);
//
//        if (approve) {
//            potMembershipService.addUserToPot(joinRequest.getRequester(), joinRequest.getPot());
//            notificationService.sendJoinApprovalNotification(joinRequest.getRequester(), joinRequest.getPot());
//        } else {
//            notificationService.sendJoinRejectionNotification(joinRequest.getRequester(), joinRequest.getPot());
//        }
//    }
//
//    // 특정 pot에 대한 모든 가입 신청 목록을 조회
//    public List<JoinRequest> getJoinRequestsByPot(Long potId) {
//        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
//        return joinRequestRepository.findByPot(pot);
//    }
//
//    // 특정 유저가 제출한 모든 가입 신청 목록을 조회
//    public List<JoinRequest> getJoinRequestsByUser(Long userId) {
//        User user = userProfileService.getUserProfileById(userId);
//        return joinRequestRepository.findByRequester(user);
//    }
}
