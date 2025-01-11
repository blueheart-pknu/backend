package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.User;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao; // 생성자 주입
    }

    @Override
    public void createUser(UserInfoDto userInfoDto) {
        // Validate input data (e.g., null checks, business rules)
        if (userInfoDto == null) {
            throw new IllegalArgumentException("UserInfoDto cannot be null");
        }

        // Check if the user already exists using a LIMIT 1 query
        boolean userExists = userDao.existsByStudentNumberAndDeletedAtIsNull(userInfoDto.getStudentNumber());
        if (userExists) {
            throw new IllegalStateException("User with the same student number already exists");
        }

        // Map UserInfoDto to User entity
        User user = User.builder()
                .username(userInfoDto.getUsername())
                .studentNumber(userInfoDto.getStudentNumber())
                .role(userInfoDto.getRole())
                .build();

        // Save the User entity to the database
        userDao.save(user); // Assuming userDao provides the save functionality
    }

    @Override
    public List<UserInfoDto> findUserByStudentNumber(String studentNumber) {
        if (studentNumber == null) {
            throw new IllegalArgumentException("Student number must not be null");
        }

        // Fetch user list and map to DTOs
        return userDao.findUsersByStudentNumberStartsWithAndDeletedAtIsNull(studentNumber)
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
        return userDao.findUsersByUsernameContainsAndDeletedAtIsNull(username)
                .map(users -> users.stream()
                        .map(user -> UserInfoDto.builder()
                                .username(user.getUsername())
                                .studentNumber(user.getStudentNumber())
                                .role(user.getRole())
                                .build())
                        .toList())
                .orElseThrow(() -> new IllegalArgumentException("Users with username '" + username + "' not found"));
    }

    @Override
    public void updateUserById(UserUpdateDto userUpdateDto) {
        // Validate input data
        if (userUpdateDto == null || userUpdateDto.getId() == null) {
            throw new IllegalArgumentException("UserUpdateDto or ID cannot be null");
        }

        // Fetch the user
        User existingUser = userDao.findUserByIdAndDeletedAtIsNull(userUpdateDto.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Update fields using updateFields
        User updatedUser = existingUser.updatedUserFields(
                userUpdateDto.getUsername().orElse(null),
                userUpdateDto.getStudentNumber().orElse(null),
                userUpdateDto.getRole().orElse(null)
        );

        // Save the updated user
        userDao.save(updatedUser);
    }

    @Override
    public void deleteUserById(UserDeleteDto userDeleteDto) {
        // Validate input data
        if (userDeleteDto == null || userDeleteDto.getId() == null) {
            throw new IllegalArgumentException("UserDeleteDto or ID cannot be null");
        }

        // Fetch the user
        User existingUser = userDao.findUserByIdAndDeletedAtIsNull(userDeleteDto.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Update the deletedAt field to mark as deleted
        User deletedUser = existingUser.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        // Save the updated user
        userDao.save(deletedUser);
    }


}