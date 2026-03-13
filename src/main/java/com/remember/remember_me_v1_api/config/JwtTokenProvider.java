package com.remember.remember_me_v1_api.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long validityInMilliseconds;

    // 생성자를 통해 yml에 설정한 값을 주입받습니다.
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long validityInMilliseconds) {
        // 문자열을 HS256 알고리즘에 적합한 SecretKey 객체로 변환
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String providerId, String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(providerId)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key) // 이제 고정된 키로 서명합니다.
                .compact();
    }
}