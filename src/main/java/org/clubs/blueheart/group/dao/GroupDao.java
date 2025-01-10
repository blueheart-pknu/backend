package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupDao extends JpaRepository<Group, Long>, GroupCustomDao {
}
