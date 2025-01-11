package org.clubs.blueheart.user.dao;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.clubs.blueheart.user.domain.UserRole;

import static org.clubs.blueheart.user.domain.QUser.user;
import static org.clubs.blueheart.user.util.UserValidationUtil.validateStudentNumber;
import static org.clubs.blueheart.user.util.UserValidationUtil.validateUsername;
import static org.clubs.blueheart.user.util.UserValidationUtil.validateUserRole;

public class UserCustomDaoImpl implements UserCustomDao {

    private JPAQueryFactory queryFactory;

    @Override
    public void updateUser(String username, String studentNumber, UserRole userRole) {
         queryFactory
            .selectFrom(user)
            .where(validateUsername(username),
                    validateStudentNumber(studentNumber),
                    validateUserRole(userRole))
            .fetch();
    }
}