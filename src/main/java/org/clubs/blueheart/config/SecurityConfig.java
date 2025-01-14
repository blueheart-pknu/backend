package org.clubs.blueheart.config;

import org.clubs.blueheart.config.jwt.JwtCookieFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCookieFilter jwtCookieFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
                http.sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http
                // CSRF 비활성화 (H2 콘솔과 Swagger를 위해)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**")
                )

                // H2 콘솔을 위한 헤더 설정
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()

                        .anyRequest().authenticated()
                )
                // JWT 필터 추가
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class)
                // 기본 로그인 폼 비활성화
                .formLogin(form -> form.disable())
                // 기본 로그아웃 비활성화
                .logout(logout -> logout.disable());

        return http.build();
    }
}