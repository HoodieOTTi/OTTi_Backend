package com.hoodie.otti.service.logout;

import com.hoodie.otti.util.login.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LogoutService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * JWT 토큰을 무효화하여 로그아웃 처리하는 메서드
     *
     * @param token 사용자 JWT 토큰
     * @return 로그아웃 성공 여부
     */
    public boolean logout(String token) {
        try {
            jwtTokenProvider.invalidateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
