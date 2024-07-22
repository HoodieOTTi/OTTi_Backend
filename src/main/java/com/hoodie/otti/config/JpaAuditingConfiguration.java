package com.hoodie.otti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.auditing.CurrentDateTimeProvider;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfiguration {

    // 사용자 인식을 위한 AuditorAware 빈 설정
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            // 인증 정보를 가져오는 코드
            return Optional.of("authenticatedUser"); // 임시로 인증된 사용자 이름 반환
        };
    }

    // 날짜 및 시간을 제공하는 DateTimeProvider 빈 설정
    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider auditingDateTimeProvider() {
        return CurrentDateTimeProvider.INSTANCE;
    }
}
