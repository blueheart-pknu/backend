
package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.User;
import org.clubs.blueheart.user.dto.UserInfoDto;

public interface UserRepository  {
    void createUser(UserInfoDto userInfoDto);

    User findOneUserByStudentNumber(Integer studentNumber);

    User findOneUserByUsername(String username);



}