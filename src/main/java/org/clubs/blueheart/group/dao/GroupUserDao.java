package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.domain.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupUserDao extends JpaRepository<GroupUser, Long>, GroupUserCustomDao {
    boolean existsByUserId(Long userId);

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    Optional<GroupUser> findByGroupIdAndUserIdAndDeletedAtIsNull(Long groupId, Long userId);

//    Optional<GroupUser> findOneByUserIdAndDeletedAtIsNull(Long userId);

    List<GroupUser> findAllByGroupIdAndDeletedAtIsNull(Long id);

}
