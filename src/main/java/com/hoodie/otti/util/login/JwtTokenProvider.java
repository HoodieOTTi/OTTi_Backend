package com.hoodie.otti.util.login;

import com.hoodie.otti.dto.login.ServiceTokenDto;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import java.util.Date;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long ACCESS_TOKEN_VALIDITY_TIME;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_VALIDITY_TIME= accessTokenValidityTime;
    }

    // 토큰 생성 메서드
    public ServiceTokenDto createToken(Long kakaoId) {

        long now = (new Date()).getTime();

        Date tokenExpiredTime = new Date(now + ACCESS_TOKEN_VALIDITY_TIME);

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(kakaoId))
                .setExpiration(tokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return ServiceTokenDto.builder()
                .accessToken(accessToken)
                .build();
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

    //  Request Header에서 토큰 값을 가져오는 메소드, "Authorization": "토큰 값"
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // 가져온 값이 비어있지 않으면서 "Bearer "로 시작한다면
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer ~값~" 형식인데, "~값~"만 가져와서 반환
            return bearerToken.substring(7);
        }
        // 아니면 null 리턴
        return null;
    }

    // 특정 토큰을 무효화하는 메서드
    public void invalidateToken(String token) {
        // 만료 시각을 현재 시각 이전으로 설정하여 토큰을 무효화합니다.
        Jwts.builder()
                .setClaims(Jwts.claims())
                .setExpiration(new Date()) // 현재 시각 이전으로 설정하여 토큰을 무효화
                .compact();
    }


    // 남은 유효기간 반환
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
