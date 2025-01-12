package org.clubs.blueheart.config.jwt;

public class JwtConstant {
    // "Bearer " (공백 포함) 로 지정
    public static final String GRANT_TYPE = "Bearer ";

    // 그 외 필요에 따라 만료 시간, 시크릿 키 등 다른 상수들도 정의 가능
    public static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60; // 예시: 1시간
    public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 예시: 7일

    // 액세스 토큰 만료 시간 예시 (30분)
    // 단위: 밀리초
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; // 30분

    // 리프레시 토큰 만료 시간 예시 (7일)
    // 단위: 밀리초
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    // ...
}