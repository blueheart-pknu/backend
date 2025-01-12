package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.domain.Group;
import org.clubs.blueheart.group.domain.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupDao extends JpaRepository<Group, Long>, GroupCustomDao {
}
