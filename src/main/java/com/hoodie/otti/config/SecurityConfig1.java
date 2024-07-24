//package com.hoodie.otti.config;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/login/kakaologin", "/kakao/callback", "/api/user/info").permitAll()
//                        .requestMatchers("/logout","/kakaoLogout", "/api/logout", "/api/unlink").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/home", true)
//                        .failureUrl("/login?error")
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/logout")
//                        .permitAll()
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.setStatus(HttpServletResponse.SC_OK);
//                        })
//                );
//
//        return http.build();
//    }
//}
