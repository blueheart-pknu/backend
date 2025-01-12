package org.clubs.blueheart.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtMiddleware {

    private final JwtProvider jwtProvider;

    public void checkJwt(HttpServletRequest request, HttpServletResponse response) {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 만들어 SecurityContext에 저장
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 유효하지 않으면 아무것도 하지 않음 (또는 예외 처리)
        // 예외 처리는 상황에 따라 custom 하게
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // 예: JwtConstant.GRANT_TYPE = "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 쿠키에서 JWT 추출 & 검증 후 userId 반환
     * 유효하지 않으면 예외를 던지거나 null 을 리턴하는 식으로 처리 가능
     */
    public Long checkJwtFromCookie(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키 중 FINAL_JWT (또는 원하는 쿠키명) 찾기
        String jwt = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("FINAL_JWT".equals(c.getName())) {
                    jwt = c.getValue();
                    break;
                }
            }
        }

        if (jwt == null) {
            throw new JwtException("JWT cookie not found");
        }

        // 2. 토큰 검증
        if (!jwtProvider.validateToken(jwt)) {
            throw new JwtException("Invalid or expired JWT");
        }

        // 3. Payload(Claims) 파싱 → userId 꺼내기
        Claims claims = jwtProvider.getClaims(jwt);
        Long userId = claims.get("userId", Long.class);
        if (userId == null) {
            throw new JwtException("No userId in token payload");
        }

        // (선택) SecurityContext 에 Authentication 설정하고 싶다면 여기서 setAuthentication(...)
        // SecurityContextHolder.getContext().setAuthentication(...)

        return userId;
    }
}