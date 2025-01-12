package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.domain.User;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao; // 생성자 주입
    }

    @Override
    public void createUser(UserInfoDto userInfoDto) {
        // Validate input data
        if (userInfoDto == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        // Check if the user already exists using a LIMIT 1 query
        boolean userExists = userDao.existsByStudentNumberAndDeletedAtIsNull(userInfoDto.getStudentNumber());
        if (userExists) {
            throw new RepositoryException(ExceptionStatus.USER_ALREADY_EXISTS);
        }

        // Map UserInfoDto to User entity
        User user = User.builder()
                .username(userInfoDto.getUsername())
                .studentNumber(userInfoDto.getStudentNumber())
                .role(userInfoDto.getRole())
                .build();

        // Save the User entity to the database
        userDao.save(user);
    }

    @Override
    public List<UserInfoDto> findUserByStudentNumber(String studentNumber) {
        if (studentNumber == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
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
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));
    }

    @Override
    public List<UserInfoDto> findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
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
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));
    }

    @Override
    public void updateUserById(UserUpdateDto userUpdateDto) {
        // Validate input data
        if (userUpdateDto == null || userUpdateDto.getId() == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        // Fetch the user
        User existingUser = userDao.findUserByIdAndDeletedAtIsNull(userUpdateDto.getId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

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
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        // Fetch the user
        User existingUser = userDao.findUserByIdAndDeletedAtIsNull(userDeleteDto.getId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        // Update the deletedAt field to mark as deleted
        User deletedUser = existingUser.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        // Save the updated user
        userDao.save(deletedUser);
    }

    @Override
    public List<UserInfoDto> findAllUser() {
        // 인스턴스 멤버인 userDao를 사용하여 메서드 호출
        Optional<List<User>> optionalUsers = userDao.findAllByDeletedAtIsNull();

        // Optional 처리: 사용자 리스트가 없으면 예외를 던짐
        List<User> users = optionalUsers.orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        // User 리스트를 UserInfoDto 리스트로 매핑
        return users.stream()
                .map(user -> UserInfoDto.builder()
                        .username(user.getUsername())
                        .studentNumber(user.getStudentNumber())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }


}