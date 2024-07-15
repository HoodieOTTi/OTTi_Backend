package com.hoodie.otti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "auditingDateTimeProvider") // JPA 감사 활성화 및 빈 참조 설정
public class JpaAuditingAndSecurityConfiguration {

    // 감사를 위한 사용자 인식 프로바이더 설정
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            // 현재 인증된 사용자 정보를 SecurityContextHolder에서 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty(); // 인증되지 않은 경우 빈 Optional 반환
            }
            return Optional.of(authentication.getName()); // 인증된 경우 사용자 이름 반환
        };
    }

    // 감사를 위한 날짜 및 시간 제공자 설정
    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now()); // 현재 날짜 및 시간을 OffsetDateTime으로 반환하는 제공자
    }
}
