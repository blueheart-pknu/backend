package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.dto.GroupInfoDto;
import org.clubs.blueheart.group.dto.GroupUserDto;
import org.clubs.blueheart.group.dto.GroupUserInfoDto;

import java.util.List;

public interface GroupRepository {
    void createGroup(GroupInfoDto groupInfoDto);

    void deleteGroup(GroupInfoDto groupInfoDto);

    void addGroupUserById(GroupUserDto groupUserDto);

    void removeGroupUserById(GroupUserDto groupUserDto);

    List<GroupUserInfoDto> getMyGroupInfoById(Long id);
}
