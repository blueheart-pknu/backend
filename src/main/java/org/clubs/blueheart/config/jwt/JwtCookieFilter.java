package org.clubs.blueheart.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.MiddlewareException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 인증이 필요 없는 경로는 필터 로직을 건너뜀
        if (path.startsWith("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1) 쿠키에서 JWT 추출
            String jwt = getJwtFromCookie(request, "FINAL_JWT");
            if (jwt != null && jwtProvider.validateToken(jwt)) {
                // 2) Claims 파싱
                Claims claims = jwtProvider.getClaims(jwt);
                Long userId = claims.get("userId", Long.class);
                if (userId == null) {
                    throw new MiddlewareException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
                }

                // 필요시 username, role 등도 claims에서 가져올 수 있음
                String username = claims.get("username", String.class);

                // 3) JwtUserDetails 생성
                JwtUserDetails userDetails = new JwtUserDetails(
                        userId,
                        username,
                        Collections.emptyList() // or 권한 생성
                );

                // 4) Authentication 객체 생성 -> SecurityContextHolder
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,    // principal
                                null,           // credentials
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 토큰이 없거나 validateToken=false 라면
                throw new MiddlewareException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
            }

        } catch (JwtException e) {
            // JWT parsing 중 에러 발생
            throw new MiddlewareException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
        }

        // 필터 체인의 다음 단계로 진행
        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}