package org.clubs.blueheart.group.application;


import org.clubs.blueheart.group.repository.GroupRepository;
import org.clubs.blueheart.group.dto.request.GroupInfoRequestDto;
import org.clubs.blueheart.group.dto.request.GroupUserRequestDto;
import org.clubs.blueheart.group.dto.response.GroupUserInfoResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void createGroup(GroupInfoRequestDto groupInfoRequestDto) {
        groupRepository.createGroup(groupInfoRequestDto);
    }

    public void deleteGroup(GroupInfoRequestDto groupInfoRequestDto) {
        groupRepository.deleteGroup(groupInfoRequestDto);
    }

    public void addGroupUserById(GroupUserRequestDto groupUserRequestDto) {
        groupRepository.addGroupUserById(groupUserRequestDto);
    }

    public void removeGroupUserById(GroupUserRequestDto groupUserRequestDto) {
        groupRepository.removeGroupUserById(groupUserRequestDto);
    }

    public List<GroupUserInfoResponseDto> getMyGroupInfoByUserId(Long id) {
        return groupRepository.getMyGroupInfoByUserId(id);
    }
}
