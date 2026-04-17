package com.rememberme.dunoesanchaeg.common.security;

import com.rememberme.dunoesanchaeg.common.exception.AuthException;
import com.rememberme.dunoesanchaeg.member.domain.enums.Role;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(Long memberId,Role role){
        Date now = new Date();

        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long memberId, Role role){
        Date now = new Date();

        Date expiration = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw new AuthException("유효하지 않은 토큰 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new AuthException("토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw new AuthException("지원되지 않는 토큰 형식입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw new AuthException("토큰이 비어있거나 잘못되었습니다.");
        }
    }

    // parseClaim을 통해 통째로 파싱
    // 필터에서 한번만 파싱해서 중복 파싱 방지
    private Claims parseClaim(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    public Long getMemberId(String token) {
//        return Long.parseLong(
//                parseClaim(token)
//                        .getSubject()
//        );
//    }
//
//    public Role getRole(String token){
//        return Role.valueOf(
//                parseClaim(token)
//                        .get("role", String.class)
//        );
//    }

    public LocalDateTime getRefreshTokenExpire(){
        // 현재 시간에 refreshTokenExpiration의 값을 더함
        return LocalDateTime.now().plus(refreshTokenExpiration, ChronoUnit.MILLIS);
    }

    public long getRefreshTokenStepSeconds() {
        return refreshTokenExpiration / 1000;
    }

    public Claims getClaims(String token) {
        return parseClaim(token);
    }

}
