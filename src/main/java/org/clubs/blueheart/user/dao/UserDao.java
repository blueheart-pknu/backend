package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends UserCustomDao, JpaRepository<User, Long> {
    User findUsersByUserName(String userName);  // JPA Query Method
    User findUsersByStudentNumber(Integer studentNumber); // JPA Query Method
}