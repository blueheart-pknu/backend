package org.clubs.blueheart.auth.api;

import org.clubs.blueheart.auth.application.AuthService;
import org.clubs.blueheart.auth.dto.request.AuthInviteAllRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthInviteOneRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthLoginRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthVerifyRequestDto;
import org.clubs.blueheart.auth.dto.response.AuthJwtResponseDto;
import org.clubs.blueheart.config.ValidationSequenceConfig;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.domain.UserRole;
import org.clubs.blueheart.user.dto.request.UserInfoRequestDto;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApi {

    private final AuthService authService;

    public AuthApi(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalResponseHandler<Void>> registerUser(@RequestBody @Validated(ValidationSequenceConfig.class) UserInfoRequestDto userInfoRequestDto) {
        authService.registerUser(userInfoRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.AUTH_USER_CREATED);
    }

    //TODO: 실제 존재하는 유저의 정보를 반환
    @PostMapping("/login")
    public ResponseEntity<GlobalResponseHandler<UserRole>> loginUser(
            @CookieValue(value = "SESSION_ID", required = false) String sessionId,
            @RequestBody AuthLoginRequestDto authLoginRequestDto
    ) {

        //TODO: Filter Layer로 변경
        authService.isSessionValid(sessionId);

        AuthJwtResponseDto authJwtResponseDto = authService.loginUserByStudentNumberAndUsername(authLoginRequestDto);

        String finalJwt = authService.createLoginJwt(authJwtResponseDto.getId(), authJwtResponseDto.getStudentNumber(), authJwtResponseDto.getUsername(), authJwtResponseDto.getRole());

        // 최종 JWT를 쿠키로 주거나, 바디로 주거나 선택
        ResponseCookie loginCookie = ResponseCookie.from("FINAL_JWT", finalJwt)
                .httpOnly(true)
                .maxAge(24 * 60 * 60 * 365) // 1년
                .path("/")
                .build();

        return GlobalResponseHandler.successWithCookie(
                ResponseStatus.AUTH_LOGGED_IN,
                authJwtResponseDto.getRole(),
                loginCookie // toString() 제거
        );
    }

    //TODO: jwt 토큰 삭제로 변경할 예정
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponseHandler<Void>> logoutUser(@RequestBody @Validated(ValidationSequenceConfig.class) AuthLoginRequestDto authLoginRequestDto) {
        authService.logoutUser(authLoginRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.AUTH_LOGGED_OUT);
    }

    //TODO: DTO로 변경
    @PostMapping("/invite/all")
    public ResponseEntity<GlobalResponseHandler<String>> inviteAllUser(@RequestBody @Validated(ValidationSequenceConfig.class) AuthInviteAllRequestDto authInviteAllRequestDto) {
        String inviteAllUrl = authService.inviteAllUser(authInviteAllRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.AUTH_INVITE_LINK_CREATED, inviteAllUrl);
    }

    @PostMapping("/invite")
    public ResponseEntity<GlobalResponseHandler<String>> inviteUserByStudentNumber(@RequestBody @Validated(ValidationSequenceConfig.class) AuthInviteOneRequestDto authInviteOneDto) {
        String inviteUrl = authService.inviteUserByStudentNumber(authInviteOneDto);
        return GlobalResponseHandler.success(ResponseStatus.AUTH_INVITE_LINK_CREATED, inviteUrl);
    }

    @PostMapping("/verify")
    public ResponseEntity<GlobalResponseHandler<Void>> verifyInviteCode(@RequestBody @Validated(ValidationSequenceConfig.class) AuthVerifyRequestDto authVerifyRequestDto) {

        String sessionId = authService.verifyInviteCode(authVerifyRequestDto);

        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", sessionId)
                .httpOnly(true)
                .maxAge(30 * 60) // 30분
                .path("/")
                .build();


        // 3) GlobalResponseHandler 로 쿠키+응답 생성
        return GlobalResponseHandler.successWithCookie(ResponseStatus.AUTH_VERIFIED, cookie);
    }
}