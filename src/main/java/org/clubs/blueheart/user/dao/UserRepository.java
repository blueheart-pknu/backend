
package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.dto.UserInfoDto;

import java.util.List;

public interface UserRepository  {

    void createUser(UserInfoDto userInfoDto);

    List<UserInfoDto> findUserByStudentNumber(String studentNumber);

    List<UserInfoDto> findUserByUsername(String username);

//    void updateUserById(UserUpdateDto userUpdateDto);
//
//    void deleteUserById(UserDeleteDto userDeleteDto);

}