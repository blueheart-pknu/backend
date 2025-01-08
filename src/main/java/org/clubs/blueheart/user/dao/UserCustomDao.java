package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.UserRole;
import org.clubs.blueheart.user.dto.UserUpdateDto;

public interface UserCustomDao {

    void updateUser(String username, String studentNumber, UserRole userRole);

}
