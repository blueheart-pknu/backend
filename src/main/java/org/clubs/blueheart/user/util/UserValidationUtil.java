package org.clubs.blueheart.user.util;


import com.querydsl.core.types.dsl.BooleanExpression;
import io.micrometer.common.util.StringUtils;
import org.clubs.blueheart.user.domain.UserRole;

import static org.clubs.blueheart.user.domain.QUser.user;

public class UserValidationUtil {
    public static BooleanExpression validateUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return user.username.eq(username);
    }

    public static BooleanExpression validateStudentNumber(String studentNumber) {
        if (studentNumber == null) {
            return null;
        }
        return user.studentNumber.eq(studentNumber);
    }

    public static BooleanExpression validateUserRole(UserRole userRole) {
        // Null 체크
        if (userRole == null) {
            return null;
        }

        // Enum 값이 올바른지 확인
        boolean isValidRole = isUserRoleValid(userRole);
        if (!isValidRole) {
            throw new IllegalArgumentException("Invalid UserRole: " + userRole);
        }

        // QueryDSL 조건 반환
        return user.role.eq(userRole);
    }

    // Enum 값 검증 메서드
    private static boolean isUserRoleValid(UserRole userRole) {
        for (UserRole role : UserRole.values()) {
            if (role == userRole) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
