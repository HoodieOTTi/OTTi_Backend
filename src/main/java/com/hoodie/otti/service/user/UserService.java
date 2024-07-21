package com.hoodie.otti.service.user;

import com.hoodie.otti.dto.login.BaseResponse;
import com.hoodie.otti.entity.login.User;
import com.hoodie.otti.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUserByEmail(String userEmail) {
        userRepository.deleteByUserEmail(userEmail);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public Optional<User> findByKakaoUserId(String kakaoUserId) {
        return userRepository.findByKakaoUserId(kakaoUserId);
    }

    public void deleteUserByKakaoUserId(String kakaoUserId) {
        userRepository.deleteByKakaoUserId(kakaoUserId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 로그아웃 처리
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 로그아웃 결과를 포함한 BaseResponse
     */
    public BaseResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 현재 인증된 사용자 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                // 인증 객체 무효화 (세션 무효화)
                SecurityContextHolder.getContext().setAuthentication(null);
                // 요청에 대한 세션 무효화
                request.getSession().invalidate();
            }

            // 쿠키나 토큰 무효화 (예: JWT)
            String token = extractToken(request);
            if (token != null) {
                // 토큰 블랙리스트 추가 로직 필요 (예: Redis에 저장)
                // tokenBlacklistService.addTokenToBlacklist(token);
            }

            return BaseResponse.success("Successfully logged out");
        } catch (Exception e) {
            return BaseResponse.failure("Logout failed: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        // 예시: Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 제거
        }
        return null;
    }
}
