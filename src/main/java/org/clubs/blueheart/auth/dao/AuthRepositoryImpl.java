package org.clubs.blueheart.auth.dao;

import org.clubs.blueheart.auth.dto.request.AuthLoginRequestDto;
import org.clubs.blueheart.auth.vo.AuthJwtVo;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.dao.UserDao;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;


@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final UserDao userDao;

    public AuthRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public AuthJwtVo findUserByStudentNumberAndUsername(AuthLoginRequestDto authLoginRequestDto) {

        if (authLoginRequestDto == null) {
            throw new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER);
        }

        // 실제로 User 엔티티(또는 null)가 반환
        User user = userDao.findOneUserByStudentNumberAndUsername(authLoginRequestDto.getStudentNumber(), authLoginRequestDto.getUsername())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.AUTH_BAD_SESSION_REQUEST));


        // 엔티티 -> AuthJwtDto 로 매핑
        return AuthJwtVo.builder()
                .id(user.getId())
                .studentNumber(user.getStudentNumber())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

}
