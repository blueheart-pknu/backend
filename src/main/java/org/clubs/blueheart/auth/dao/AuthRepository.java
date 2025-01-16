package org.clubs.blueheart.auth.dao;

import org.clubs.blueheart.auth.dto.request.AuthLoginRequestDto;
import org.clubs.blueheart.auth.vo.AuthJwtVo;

public interface AuthRepository {
    AuthJwtVo findUserByStudentNumberAndUsername(AuthLoginRequestDto authLoginRequestDto);
}
