package org.clubs.blueheart.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * JWT 파싱 후 얻은 정보를 담는 UserDetails 구현체
 */
@Getter
@AllArgsConstructor
public class JwtUserDetails implements UserDetails {

    private Long userId;            // JWT에 들어 있는 유저 식별값
    private String username;        // 필요시, 페이로드에 담긴 값
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER 등 권한 목록
        return authorities != null ? authorities : Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null; // JWT 인증에선 패스워드 사용 X
    }

    @Override
    public String getUsername() {
        // username이 없으면 userId 등 대체
        return (username != null) ? username : String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}