package org.clubs.blueheart.auth.application;

import org.clubs.blueheart.auth.dao.AuthRepository;
import org.clubs.blueheart.auth.dto.AuthDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.springframework.stereotype.Service;


//TODO: 초대 URL 만드는 방법 논의필요 (jwt같은 형태 생각중)
@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void loginUserByStudentNumberAndUsername(AuthDto authDto) {
    }

    public void logoutUser(AuthDto authDto) {
    }

    public void registerUser(UserInfoDto userInfoDto) {
    }

    public String inviteAllUser() {
        return "";
    }

    public String inviteUserByStudentNumber(AuthDto authDto) {
        return "";
    }
}

