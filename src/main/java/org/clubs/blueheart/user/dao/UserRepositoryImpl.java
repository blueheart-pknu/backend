package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.User;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;
    private UserCustomDaoImpl userCustomDaoImpl;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao; // 생성자 주입
    }

    @Override
    public void createUser(UserInfoDto userInfoDto) {
        // Validate input data (e.g., null checks, business rules)
        if (userInfoDto == null) {
            throw new IllegalArgumentException("UserInfoDto cannot be null");
        }

        // Map UserInfoDto to User entity
        User user = User.builder()
                .username(userInfoDto.getUsername())
                .studentNumber(userInfoDto.getStudentNumber())
                .role(userInfoDto.getRole()) // Assuming UserInfoDto contains a role field
                .build();

        // Save the User entity to the database
        userDao.save(user); // Assuming userCustomDao provides the save functionality
    }

    @Override
    public List<UserInfoDto> findUserByStudentNumber(Integer studentNumber) {
        if (studentNumber == null) {
            throw new IllegalArgumentException("Student number must not be null");
        }

        // Fetch user list and map to DTOs
        return userDao.findUsersByStudentNumber(studentNumber)
                .map(users -> users.stream()
                        .map(user -> UserInfoDto.builder()
                                .username(user.getUsername())
                                .studentNumber(user.getStudentNumber())
                                .role(user.getRole())
                                .build())
                        .toList())
                .orElseThrow(() -> new IllegalArgumentException("Users with student number " + studentNumber + " not found"));
    }


    // trim => 공백만 포함된 문자열 처리
    @Override
    public List<UserInfoDto> findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }

        // Fetch user list and map to DTOs
        return userDao.findUsersByUsername(username)
                .map(users -> users.stream()
                        .map(user -> UserInfoDto.builder()
                                .username(user.getUsername())
                                .studentNumber(user.getStudentNumber())
                                .role(user.getRole())
                                .build())
                        .toList())
                .orElseThrow(() -> new IllegalArgumentException("Users with username '" + username + "' not found"));
    }

//    @Override
//    public void updateUserById(UserUpdateDto userUpdateDto) {
//
//        userCustomDaoImpl.updateUser();
//
//    }
//
//    @Override
//    public void deleteUserById(UserDeleteDto userDeleteDto) {
//
//    }


}