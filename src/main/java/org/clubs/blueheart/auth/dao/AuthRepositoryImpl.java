package org.clubs.blueheart.auth.dao;

import org.clubs.blueheart.auth.dto.AuthJwtDto;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.dao.UserDao;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final UserDao userDao;

    public AuthRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public AuthJwtDto findUserByStudentNumberAndUsername(String studentNumber, String username) {

        if (studentNumber == null || username == null) {
            throw new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER);
        }

        // 실제로 User 엔티티(또는 null)가 반환
        User user = userDao.findOneUserByStudentNumberAndUsername(studentNumber, username)
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.AUTH_BAD_SESSION_REQUEST));


        // 엔티티 -> AuthJwtDto 로 매핑
        return AuthJwtDto.builder()
                .id(user.getId())
                .studentNumber(user.getStudentNumber())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

}
