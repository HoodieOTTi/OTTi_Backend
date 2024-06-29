package com.hoodie.otti.util.login;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {

    // 토큰 생성 메서드
    public String createToken(String userEmail) {
        // 실제 토큰 생성 로직을 구현합니다.
        return Jwts.builder()
                .setSubject(userEmail)
                .signWith(SignatureAlgorithm.HS256, "your_secret_key")
                .compact();
    }

    // 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey("your_secret_key").parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 정보 추출 메서드
    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("your_secret_key")
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}

