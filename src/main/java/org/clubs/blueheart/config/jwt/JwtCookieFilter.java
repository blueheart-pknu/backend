package org.clubs.blueheart.config.jwt;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        // /api/v1/auth/**, /h2-console/**, Swagger 관련 경로 제외
        if (path.startsWith("/api/v1/auth/register")
                || path.startsWith("/api/v1/auth/login")
                || path.startsWith("/api/v1/auth/verify")
                || path.startsWith("/h2-console/")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")) {
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
                        throw new MiddlewareException(ExceptionStatus.GENERAL_BAD_REQUEST);
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
                }

            } catch (MalformedJwtException e) {
                throw new MiddlewareException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
            } catch (JwtException e) {
                throw new MiddlewareException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
            }
        }

        filterChain.doFilter(request, response);
    }
}