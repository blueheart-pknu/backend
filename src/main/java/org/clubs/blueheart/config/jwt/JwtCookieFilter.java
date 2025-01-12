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

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                if (jwtProvider.validateToken(jwt)) {
                    Claims claims = jwtProvider.getClaims(jwt);
                    Long userId = claims.get("userId", Long.class);
                    if (userId == null) {
                        throw new MiddlewareException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
                    }

                    String username = claims.get("username", String.class);

                    JwtUserDetails userDetails = new JwtUserDetails(
                            userId,
                            username,
                            Collections.emptyList()
                    );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MiddlewareException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
                }
            } catch (JwtException e) {
                throw new MiddlewareException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
            }
        }

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