package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.UserRole;

public interface UserCustomDao {

    void updateUser(String username, String studentNumber, UserRole userRole);
}