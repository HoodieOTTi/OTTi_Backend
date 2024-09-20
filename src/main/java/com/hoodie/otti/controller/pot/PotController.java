package com.hoodie.otti.controller.pot;

import com.hoodie.otti.dto.pot.*;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.service.pot.JoinRequestService;
import com.hoodie.otti.service.pot.PotMembershipService;
import com.hoodie.otti.service.pot.PotService;
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

@RestController
@RequestMapping("/api/pot")
public class PotController {
    @Autowired
    private JoinRequestService joinRequestService;

    @Autowired
    private PotService potService;

    @Autowired
    private PotMembershipService potMembershipService;

    @PostMapping("/create")
    public ResponseEntity<Void> createPot(@RequestBody PotSaveRequestDto requestDto, Principal principal) {
        Long id = potService.save(requestDto, principal);
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

    @PostMapping("/application/joinrequest")
    public ResponseEntity<Void> requestJoin(Principal principal, @RequestParam Long potId, @RequestBody JoinRequestDescriptionDTO joinRequestDescriptionDTO) {
        Pot pot = potService.findById(potId);
        String joinrequestDescription = joinRequestDescriptionDTO.getJoinrequestDescription();
        joinRequestService.createJoinRequest(principal, pot, joinRequestDescriptionDTO);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/application/approve")
    public ResponseEntity<Void> approveJoinRequest(Principal principal, @RequestParam Long potId) {
        Pot pot = potService.findById(potId);
        joinRequestService.handleJoinRequest(principal, pot, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/application/reject")
    public ResponseEntity<Void> rejectJoinRequest(Principal principal, @RequestParam Long potId) {
        Pot pot = potService.findById(potId);
        joinRequestService.handleJoinRequest(principal, pot, false);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(Principal principal, @RequestParam Long potId) {
        Pot pot = potService.findById(potId);
        potMembershipService.removeUserFromPot(principal, pot);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @RequestParam Long potId, Principal principal) {
        Pot pot = potService.findById(potId);
        potMembershipService.removeUserFromPotByCreator(principal, userId, pot);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/applications/member/{potId}")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsByPot(@PathVariable Long potId) {
        List<JoinRequestDTO> joinRequestDTOs = joinRequestService.getJoinRequestsByPot(potId);
        return ResponseEntity.ok(joinRequestDTOs);
    }

    @GetMapping("/applications/user/{userId}")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsByUser(@PathVariable Long userId) {
        List<JoinRequestDTO> joinRequestDTOs = joinRequestService.getJoinRequestsByUser(userId);
        return ResponseEntity.ok(joinRequestDTOs);
    }

    @GetMapping("/applications/user")
    public ResponseEntity<List<JoinRequestDTO>>getJoinRequestsByPrincipal(Principal principal) {
        List<JoinRequestDTO> joinRequestDTOs = joinRequestService.getJoinRequestsByPrincipal(principal);
        return ResponseEntity.ok(joinRequestDTOs);
    }

    @GetMapping("/application/pot/{potId}/users/approve")
    public ResponseEntity<List<PotMembershipDTO>> getApproveJoinRequestsByPot(@PathVariable Long potId) {
        List<PotMembershipDTO> potMemberships = potMembershipService.getApprovedMembersByPotId(potId);
        return ResponseEntity.ok(potMemberships);
    }

    @GetMapping("/application/user/pots/approve")
    public ResponseEntity<List<PotMembershipDTO>> getApproveJoinRequestsByUser(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<PotMembershipDTO> potMemberships = potMembershipService.getApprovedPotsByUserId(userId);
        return ResponseEntity.ok(potMemberships);
    }

    @GetMapping("/application/user/pots/permission")
    public ResponseEntity<List<PotMembershipDTO>> hasPermissionJoinRequestsByUser(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<PotMembershipDTO> potMemberships = potMembershipService.hasPermissionPotsByUserId(userId);
        return ResponseEntity.ok(potMemberships);
    }

    @GetMapping("/application/joinrequest/pots/permission")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsForUserPots(Principal principal) {
        List<JoinRequestDTO> joinRequests = potMembershipService.getJoinRequestsForUserPots(principal);
        return ResponseEntity.ok(joinRequests);
    }

    @GetMapping("/application/user/pots/approve/permission")
    public ResponseEntity<List<PotMembershipDTO>> getApproveOrPermissionJoinRequestsByUser(Principal principal) {
        List<PotMembershipDTO> pots = potMembershipService.getApproveOrPermissionJoinRequestsByUser(principal);
        return ResponseEntity.ok(pots);
    }


}

