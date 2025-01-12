package org.clubs.blueheart.auth.api;

import jakarta.validation.Valid;
import org.clubs.blueheart.auth.application.AuthService;
import org.clubs.blueheart.auth.dto.AuthDto;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.dto.UserInfoDto;
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

    @PostMapping("/login")
    public ResponseEntity<GlobalResponseHandler<Void>> loginUser(@RequestBody @Valid AuthDto authDto) {
        authService.loginUserByStudentNumberAndUsername(authDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_CREATED);
    }

    //TODO: jwt 토큰 삭제로 변경할 예정
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponseHandler<Void>> logoutUser(@RequestBody @Valid AuthDto authDto) {
        authService.logoutUser(authDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_CREATED);
    }

    //TODO: DTO로 변경
    @PostMapping("/invite/all")
    public ResponseEntity<GlobalResponseHandler<String>> inviteAllUser() {
        String inviteAllUrl = authService.inviteAllUser();
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UPDATED, inviteAllUrl);
    }

    @PostMapping("/invite")
    public ResponseEntity<GlobalResponseHandler<String>> inviteUserByStudentNumber(@RequestBody @Valid AuthDto authDto) {
        String inviteUrl = authService.inviteUserByStudentNumber(authDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UPDATED, inviteUrl);
    }
}