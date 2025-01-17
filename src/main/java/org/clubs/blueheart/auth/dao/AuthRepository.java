package org.clubs.blueheart.auth.dao;

import org.clubs.blueheart.auth.dto.request.AuthLoginRequestDto;
import org.clubs.blueheart.auth.dto.response.AuthJwtResponseDto;

public interface AuthRepository {
    AuthJwtResponseDto findUserByStudentNumberAndUsername(AuthLoginRequestDto authLoginRequestDto);
}
