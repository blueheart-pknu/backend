package org.clubs.blueheart.config.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
    private final Key key;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public JwtGenerator() {
        // .env를 읽어오는 부분
        Dotenv dotenv = Dotenv.load();

        String secretKey = dotenv.get("JWT_SECRET");
        this.accessTokenExpireTime = Long.parseLong(dotenv.get("ACCESS_TOKEN_EXPIRE_TIME"));
        this.refreshTokenExpireTime = Long.parseLong(dotenv.get("REFRESH_TOKEN_EXPIRE_TIME"));

        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        this.key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * payload(Claim)와 만료시간을 받아 JWT 생성
     */
    public String createToken(Map<String, Object> payload, long expireMillis) {
        long now = System.currentTimeMillis();
        Date expireDate = new Date(now + expireMillis);

        return Jwts.builder()
                .setClaims(payload)      // payload 전체를 map 형태로 설정
                .setIssuedAt(new Date()) // 생성 시각
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 파싱/검증 후 Claims 반환
     */
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}