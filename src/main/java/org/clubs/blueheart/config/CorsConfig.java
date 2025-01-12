package org.clubs.blueheart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 클라이언트의 도메인 설정
        config.setAllowedOrigins(List.of("http://localhost:8081", "http://127.0.0.1:8081")); // 올바른 설정 // 실제 클라이언트 도메인으로 변경

        // 쿠키를 포함한 요청 허용
        config.setAllowCredentials(true);

        // 허용할 헤더 설정
        config.addAllowedHeader("*"); // 모든 헤더 허용

        // 허용할 HTTP 메서드 설정
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용

        // 필요한 경우 추가 설정
        // 예: config.addExposedHeader("Authorization"); // 특정 헤더 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}