package org.clubs.blueheart.activity.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.user.domain.UserRole;
import org.springframework.stereotype.Repository;

import static org.clubs.blueheart.activity.domain.QActivity.activity;
import static org.clubs.blueheart.user.util.UserValidationUtil.*;

public class ActivityCustomDaoImpl implements ActivityCustomDao {

    private JPAQueryFactory queryFactory;

//    @Override
//    public void updateActivityById(String username, String studentNumber, UserRole userRole) {
//        queryFactory
//                .selectFrom(activity)
//                .where(validateUsername(username),
//                        validateStudentNumber(studentNumber),
//                        validateUserRole(userRole))
//                .fetch();
//    }

}