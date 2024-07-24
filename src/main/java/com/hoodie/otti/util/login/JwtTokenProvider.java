package com.hoodie.otti.util.login;

import com.hoodie.otti.dto.login.KakaoTokenDto;
import com.hoodie.otti.dto.login.ServiceTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

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
                .claim("auth", "ROLE_USER")
                .setExpiration(tokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return ServiceTokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    // 토큰에 담겨있는 정보를 가져오는 메소드
    public Authentication getAuthentication(String serviceAccessToken) {
        Claims claims = parseClaims(serviceAccessToken);

        if (claims.get("auth") == null) {
            throw new IllegalArgumentException("권한 없음");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
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

    private Claims parseClaims(String serviceAccessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(serviceAccessToken).getBody();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우 만료된 클레임을 반환
            return e.getClaims();
        } catch (MalformedJwtException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // 남은 유효기간 반환
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }


    // refreshToken을 이용한 생명 연장
    public KakaoTokenDto.ServiceToken createAccessTokenByRefreshToken(HttpServletRequest request, String refreshToken) {
        String[] chunks = resolveToken(request).split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        String name = payload.split("\"")[3];

        // 현재 시간
        long now = (new Date()).getTime();

        // AccessToken 유효 기간
        Date tokenExpiredTime = new Date(now + ACCESS_TOKEN_VALIDITY_TIME);

        // AccessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim("auth", "ROLE_USER")
                .setExpiration(tokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // TokenDTO 형태로 반환
        return KakaoTokenDto.ServiceToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
