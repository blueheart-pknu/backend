package org.clubs.blueheart.group.application;


import org.clubs.blueheart.group.dao.GroupRepository;
import org.clubs.blueheart.group.dao.GroupUserDao;
import org.clubs.blueheart.group.dto.GroupInfoDto;
import org.clubs.blueheart.group.dto.GroupUserDto;
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
        groupRepository.createGroup(groupInfoDto);
    }

    public void deleteGroup(GroupInfoDto groupInfoDto) {
        groupRepository.deleteGroup(groupInfoDto);
    }

    public void addGroupUserById(GroupUserDto groupUserDto) {
        groupRepository.addGroupUserById(groupUserDto);
    }

    public void removeGroupUserById(GroupUserDto groupUserDto) {
        groupRepository.removeGroupUserById(groupUserDto);
    }

    public List<GroupUserInfoDto> getMyGroupInfoById(Long id) {
        return groupRepository.getMyGroupInfoById(id);
    }
}
