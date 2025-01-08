package org.clubs.blueheart.user.dao;

import org.clubs.blueheart.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends UserCustomDao, JpaRepository<User, Long> {
    Optional<List<User>> findUsersByUsernameContains(String userName);  // JPA Query Method
    Optional<List<User>> findUsersByStudentNumberStartsWith(String studentNumber); // JPA Query Method
}