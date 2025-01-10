package org.clubs.blueheart.group.application;


import org.clubs.blueheart.group.dao.GroupRepository;
import org.clubs.blueheart.group.dto.GroupInfoDto;
import org.clubs.blueheart.group.dto.GroupUserInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void createGroup(GroupInfoDto groupInfoDto) {

    }

    public void deleteGroup(GroupInfoDto groupInfoDto) {
    }

    public void addGroupUserById(Long id) {
    }

    public void removeGroupUserById(Long id) {
    }

    public List<GroupUserInfoDto> getMyGroupInfoById(Long id) {
        return List.of();
    }
}
