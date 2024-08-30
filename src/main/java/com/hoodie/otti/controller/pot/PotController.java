package com.hoodie.otti.controller.pot;

import com.hoodie.otti.dto.pot.JoinRequestDTO;
import com.hoodie.otti.dto.pot.PotJoinRequestDTO;
import com.hoodie.otti.dto.pot.PotSaveRequestDto;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.pot.PotMembership;
import com.hoodie.otti.repository.pot.PotMembershipRepository;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.service.pot.JoinRequestService;
import com.hoodie.otti.service.pot.PotMembershipService;
import com.hoodie.otti.service.pot.PotService;
import com.hoodie.otti.service.profile.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/pot")
public class PotController {
    @Autowired
    private JoinRequestService joinRequestService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PotService potService;

    @Autowired
    private PotRepository potRepository;

    @Autowired
    private PotMembershipRepository potMembershipRepository;

    @Autowired
    private PotMembershipService potMembershipService;

    @PostMapping("/create")
    public ResponseEntity<Void> createPot(@RequestBody PotSaveRequestDto requestDto) {
        Long id = potService.save(requestDto);
        return ResponseEntity.created(URI.create("/api/pot/create/" + id)).build();
    }

    @GetMapping("/create/{potId}")
    public ResponseEntity<PotJoinRequestDTO> getPotById(@PathVariable Long potId) {
        PotJoinRequestDTO potJoinRequestDTO = potService.findPotById(potId);
        return ResponseEntity.ok(potJoinRequestDTO);
    }

    @PutMapping("/{potId}")
    public ResponseEntity<Void> updatePot(
            @PathVariable Long potId,
            @RequestBody @Valid PotSaveRequestDto requestDto) {
        try {
            potService.updatePot(potId, requestDto);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "팟 업데이트에 실패했습니다", e);
        }
    }

    @DeleteMapping("/{potId}")
    public ResponseEntity<Void> deletePot(@PathVariable Long potId) {
        potService.deletePot(potId);
        return ResponseEntity.noContent().build();
    }

    // 특정 pot에 가입 신청 처리
    @PostMapping("/application/joinrequest")
    public ResponseEntity<Void> requestJoin(Principal principal, @RequestParam Long potId) {
        Pot pot = potService.findById(potId);
        joinRequestService.createJoinRequest(principal, pot);
        return ResponseEntity.ok().build();
    }

    // 가입 신청 승인
    @PostMapping("/application/approve")
    public ResponseEntity<Void> approveJoinRequest(Principal principal, @RequestParam Long potId) {
        Pot pot = potService.findById(potId);
        joinRequestService.handleJoinRequest(principal, pot, true);
        return ResponseEntity.ok().build();
    }

    // 가입 신청 거절
    @PostMapping("/application/reject")
    public ResponseEntity<Void> rejectJoinRequest(Principal principal, @RequestParam Long potId) {
        Pot pot = potService.findById(potId);
        joinRequestService.handleJoinRequest(principal, pot, false);
        return ResponseEntity.ok().build();
    }

    // 특정 pot에 대한 모든 가입 신청 목록
    @GetMapping("/applications/member/{potId}")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsByPot(@PathVariable Long potId) {
        List<JoinRequestDTO> joinRequestDTOs = joinRequestService.getJoinRequestsByPot(potId);
        return ResponseEntity.ok(joinRequestDTOs);
    }


    // 특정 user에 대한 모든 팟 가입 신청 목록
    @GetMapping("/applications/user/{userId}")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsByUser(@PathVariable Long userId) {
        List<JoinRequestDTO> joinRequestDTOs = joinRequestService.getJoinRequestsByUser(userId);
        return ResponseEntity.ok(joinRequestDTOs);
    }


    // 특정 pot에 포함된 전체 user 목록 조회 API
    @GetMapping("/application/pot/{potId}/users/approve")
    public ResponseEntity<List<PotMembership>> getApproveJoinRequestsByPot(@PathVariable Long potId) {
        List<PotMembership> potMemberships = potMembershipService.getApprovedMembersByPotId(potId);
        return ResponseEntity.ok(potMemberships);
    }

    // 인증된 사용자가 권한을 지닌 전체 pot 목록 조회 API
    @GetMapping("/application/user/pots/approve")
    public ResponseEntity<List<PotMembership>> getApproveJoinRequestsByUser(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<PotMembership> potMemberships = potMembershipService.getApprovedPotsByUserId(userId);
        return ResponseEntity.ok(potMemberships);
    }

}

