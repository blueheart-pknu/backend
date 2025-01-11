package org.clubs.blueheart.group.api;

import jakarta.validation.Valid;
import org.clubs.blueheart.group.application.GroupService;
import org.clubs.blueheart.group.dto.GroupUserDto;
import org.clubs.blueheart.group.dto.GroupInfoDto;
import org.clubs.blueheart.group.dto.GroupUserInfoDto;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
public class GroupApi {

    private final GroupService groupService;

    public GroupApi(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalResponseHandler<Void>> createGroup(@RequestBody @Valid GroupInfoDto groupInfoDto) {
        groupService.createGroup(groupInfoDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SUBSCRIBED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GlobalResponseHandler<Void>> deleteGroup(@RequestBody @Valid GroupInfoDto groupInfoDto) {
        groupService.deleteGroup(groupInfoDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UNSUBSCRIBED);
    }

    //TODO: jwt로 groupId 대체 예정
    @PostMapping("/add")
    public ResponseEntity<GlobalResponseHandler<Void>> addGroupUser(@RequestBody @Valid GroupUserDto groupUserDto) {
        groupService.addGroupUserById(groupUserDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SUBSCRIBED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<GlobalResponseHandler<Void>> removeGroupUser(@RequestBody @Valid GroupUserDto groupUserDto) {
        groupService.removeGroupUserById(groupUserDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UNSUBSCRIBED);
    }

    //TODO: 추후 jwt기반으로 변경할 예정
    @GetMapping("/me/{id}")
    public ResponseEntity<GlobalResponseHandler<List<GroupUserInfoDto>>> getMyGroupInfo(@PathVariable Long id) {
        List<GroupUserInfoDto> groupUserInfo = groupService.getMyGroupInfoById(id);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, groupUserInfo);
    }
}