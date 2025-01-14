package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.domain.GroupUser;

import java.util.Optional;

public interface GroupUserCustomDao {
    public Optional<GroupUser> findOneByUserIdAndDeletedAtIsNull(Long userId);
}
