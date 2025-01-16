
package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.dto.request.UserDeleteRequestDto;
import org.clubs.blueheart.user.dto.request.UserInfoRequestDto;
import org.clubs.blueheart.user.dto.request.UserUpdateRequestDto;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;

import java.util.List;

public interface UserRepository  {

    void createUser(UserInfoRequestDto userInfoRequestDto);

    List<UserInfoResponseDto> findUserByStudentNumber(String studentNumber);

    List<UserInfoResponseDto> findUserByUsername(String username);

    void updateUserById(UserUpdateRequestDto userUpdateRequestDto);

    void deleteUserById(UserDeleteRequestDto userDeleteRequestDto);

    List<UserInfoResponseDto> findAllUser();
}