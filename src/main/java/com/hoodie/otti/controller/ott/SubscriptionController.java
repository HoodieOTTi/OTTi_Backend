package com.hoodie.otti.controller.ott;

import com.hoodie.otti.dto.ott.SubscriptionResponseDto;
import com.hoodie.otti.dto.ott.SubscriptionSaveRequestDto;
import com.hoodie.otti.service.ott.SubscriptionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public Long save(@RequestBody SubscriptionSaveRequestDto requestDto) {
        return subscriptionService.save(requestDto);
    }

    @GetMapping
    public List<SubscriptionResponseDto> findAllSubscription() {
        return subscriptionService.findAll()
                .stream()
                .map(SubscriptionResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SubscriptionResponseDto findById(@PathVariable Long id) {
        return new SubscriptionResponseDto(subscriptionService.findById(id));
    }
}
