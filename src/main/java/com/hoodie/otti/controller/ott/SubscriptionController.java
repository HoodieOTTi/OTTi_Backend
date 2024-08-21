package com.hoodie.otti.controller.ott;

import com.hoodie.otti.dto.ott.SubscriptionByUserResponseDto;
import com.hoodie.otti.dto.ott.SubscriptionDDayResponseDto;
import com.hoodie.otti.dto.ott.SubscriptionResponseDto;
import com.hoodie.otti.dto.ott.SubscriptionRequestDto;
import com.hoodie.otti.service.ott.SubscriptionService;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody SubscriptionRequestDto requestDto, Principal principal) {
        Long id = subscriptionService.save(requestDto, principal);
        return ResponseEntity.created(URI.create("/api/subscription/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> findAllSubscription() {
        List<SubscriptionResponseDto> subscriptionResponseDtos = subscriptionService.findAll()
                .stream()
                .map(SubscriptionResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(subscriptionResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(new SubscriptionResponseDto(subscriptionService.findById(id)));
    }

    @GetMapping("/user")
    public ResponseEntity<List<SubscriptionByUserResponseDto>> findByUserId(Principal principal) {
        return ResponseEntity.ok().body(subscriptionService.findAllByUserId(principal)
                .stream()
                .map(SubscriptionByUserResponseDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/d-day/{id}")
    public ResponseEntity<SubscriptionDDayResponseDto> getDDay(@PathVariable Long id) {
        return ResponseEntity.ok().body(new SubscriptionDDayResponseDto(subscriptionService.calculateDDay(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody SubscriptionRequestDto requestDto) {
        subscriptionService.update(id, requestDto);
        return ResponseEntity.ok().body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
