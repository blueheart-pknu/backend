package org.clubs.blueheart.auth.dao;

import org.clubs.blueheart.auth.dto.AuthDto;
import org.clubs.blueheart.auth.dto.AuthJwtDto;

public interface AuthRepository {
    AuthJwtDto findUserByStudentNumberAndUsername(AuthDto authDto);
}
