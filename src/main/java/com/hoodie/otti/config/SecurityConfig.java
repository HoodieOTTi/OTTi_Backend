package com.hoodie.otti.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/home", "/", "/login").permitAll()
                        .requestMatchers("/login/kakaologin", "/kakao/callback").permitAll()
                        .requestMatchers("/api/logout", "/api/delete-user", "/login?error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")  // 로그인 실패 시 리다이렉트 URL
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")  // 로그아웃 요청 URL
                        .logoutSuccessUrl("/")  // 로그아웃 성공 후 리다이렉트 URL
                        .permitAll()
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 로그아웃 성공 시 HTTP 상태 코드 설정
                            response.setStatus(HttpServletResponse.SC_OK);
                            // 로그아웃 성공 후 클라이언트 측에서 처리하도록 리다이렉트
                            response.sendRedirect("/");
                        })
                );

        return http.build();
    }
}
