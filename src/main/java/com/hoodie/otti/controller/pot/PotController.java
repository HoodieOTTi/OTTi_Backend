package com.hoodie.otti.controller.pot;

import com.hoodie.otti.dto.pot.PotSaveRequestDto;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.service.pot.JoinRequestService;
import com.hoodie.otti.service.pot.PotService;
import com.hoodie.otti.service.profile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/api/pot")
public class PotController {
    @Autowired
    private JoinRequestService joinRequestService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PotService potService;

    @PostMapping("/create")
    public ResponseEntity<Void> createPot(@RequestBody PotSaveRequestDto requestDto) {
        Long id = potService.save(requestDto);
        return ResponseEntity.created(URI.create("/api/pot/create/" + id)).build();
    }

    @GetMapping("/create/{potId}")
    public ResponseEntity<Pot> getPotById(@PathVariable Long potId) {
        Pot pot = potService.findPotById(potId);
        return ResponseEntity.ok(pot);
    }

//    @PutMapping("/{potId}")
//    public ResponseEntity<Pot> deletePot(@RequestBody PotSaveRequestDto requestDto){
//        return
//    }


    // 특정 pot에 가입 신청 처리
//    @PostMapping("/application/joinrequest")
//    public ResponseEntity<Void> requestJoin(@RequestParam Long userId, @RequestParam Long potId) {
//        User requester = userProfileService.getUserProfileById(userId);
//        Pot pot = potService.findById(potId);
//        joinRequestService.createJoinRequest(requester, pot);
//        return ResponseEntity.ok().build();
//    }
//
//    // 가입 신청 승인
//    @PostMapping("/application/{requestId}/approve")
//    public ResponseEntity<Void> approveJoinRequest(@PathVariable Long requestId, @RequestParam Long adminId) {
//        joinRequestService.handleJoinRequest(requestId, adminId, true);
//        return ResponseEntity.ok().build();
//    }
//
//    // 가입 신청 거절
//    @PostMapping("/application/{requestId}/reject")
//    public ResponseEntity<Void> rejectJoinRequest(@PathVariable Long requestId, @RequestParam Long adminId) {
//        joinRequestService.handleJoinRequest(requestId, adminId, false);
//        return ResponseEntity.ok().build();
//    }
//
//
//    // 특정 pot에 대한 모든 가입 신청 목록
//    @GetMapping("/applications/member/{potId}")
//    public ResponseEntity<List<JoinRequest>> getJoinRequests(@PathVariable Long potId) {
//        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsByPot(potId);
//        return ResponseEntity.ok(joinRequests);
//    }
//
//    // 특정 user에 대한 모든 팟 가입 신청 목록
//    @GetMapping("/applications/user/{userId}")
//    public ResponseEntity<List<JoinRequest>> getJoinRequestsByUser(@PathVariable Long userId) {
//        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsByUser(userId);
//        return ResponseEntity.ok(joinRequests);
//    }

}

