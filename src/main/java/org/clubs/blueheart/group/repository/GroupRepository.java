package org.clubs.blueheart.group.repository;

import org.clubs.blueheart.group.dto.request.GroupInfoRequestDto;
import org.clubs.blueheart.group.dto.request.GroupUserRequestDto;
import org.clubs.blueheart.group.dto.response.GroupUserInfoResponseDto;

import java.util.List;

public interface GroupRepository {
    void createGroup(GroupInfoRequestDto groupInfoRequestDto);

    void deleteGroup(GroupInfoRequestDto groupInfoRequestDto);

    void addGroupUserById(GroupUserRequestDto groupUserRequestDto);

    void removeGroupUserById(GroupUserRequestDto groupUserRequestDto);

    List<GroupUserInfoResponseDto> getMyGroupInfoById(Long id);
}
