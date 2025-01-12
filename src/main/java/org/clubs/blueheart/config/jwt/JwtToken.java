package org.clubs.blueheart.config.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    // 필요한 경우, 만료 시간이나 사용자 정보 등 더 많은 필드를 추가할 수 있습니다.
}