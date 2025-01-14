package org.clubs.blueheart.auth.api;

import jakarta.validation.Valid;
import org.clubs.blueheart.auth.application.AuthService;
import org.clubs.blueheart.auth.dto.AuthDto;
import org.clubs.blueheart.auth.dto.AuthInviteAllDto;
import org.clubs.blueheart.auth.dto.AuthInviteOneDto;
import org.clubs.blueheart.auth.dto.AuthVerifyDto;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApi {

    private final AuthService authService;

    public AuthApi(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalResponseHandler<Void>> registerUser(@RequestBody @Valid UserInfoDto userInfoDto) {
        authService.registerUser(userInfoDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_CREATED);
    }

    //TODO: 실제 존재하는 유저의 정보를 반환
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @CookieValue(value = "SESSION_ID", required = false) String sessionId,
            @RequestBody AuthDto authDto
    ) {
        // 1. 세션 체크
        if (!authService.isSessionValid(sessionId)) {
            return ResponseEntity.status(401).body("Session invalid or expired");
        }

        // 2. 학번, 이름 검증
        if (!authService.checkUserInfo(authDto.getStudentNumber(), authDto.getUsername())) {
            return ResponseEntity.badRequest().body("Invalid student number or username");
        }

        // 3. DB나 로직상 userId=123, role="USER" 라고 가정
        Long userId = 1L;
        String role = "ADMIN";
        String finalJwt = authService.createLoginJwt(userId, authDto.getStudentNumber(), authDto.getUsername(), role);

        // 최종 JWT를 쿠키로 주거나, 바디로 주거나 선택
        ResponseCookie loginCookie = ResponseCookie.from("FINAL_JWT", finalJwt)
                .httpOnly(true)
                .maxAge(24 * 60 * 60 * 365) // 1년
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", loginCookie.toString())
                .body(role);
    }

    //TODO: jwt 토큰 삭제로 변경할 예정
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponseHandler<Void>> logoutUser(@RequestBody @Valid AuthDto authDto) {
        authService.logoutUser(authDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_CREATED);
    }

    //TODO: DTO로 변경
    @PostMapping("/invite/all")
    public ResponseEntity<GlobalResponseHandler<String>> inviteAllUser(@RequestBody @Valid AuthInviteAllDto authInviteAllDto) {
        String inviteAllUrl = authService.inviteAllUser(authInviteAllDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UPDATED, inviteAllUrl);
    }

    @PostMapping("/invite")
    public ResponseEntity<GlobalResponseHandler<String>> inviteUserByStudentNumber(@RequestBody @Valid AuthInviteOneDto authInviteOneDto) {
        String inviteUrl = authService.inviteUserByStudentNumber(authInviteOneDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UPDATED, inviteUrl);
    }

    @PostMapping("/verify")
    public ResponseEntity<GlobalResponseHandler<Void>> verifyInviteCode(@RequestBody @Valid AuthVerifyDto authVerifyDto) {

        String sessionId = authService.verifyInviteCode(authVerifyDto);

        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", sessionId)
                .httpOnly(true)
                .maxAge(30 * 60) // 30분
                .path("/")
                .build();


        // 3) GlobalResponseHandler 로 쿠키+응답 생성
        return GlobalResponseHandler.successWithCookie(ResponseStatus.ACTIVITY_UPDATED, cookie);
    }
}