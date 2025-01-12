package org.clubs.blueheart.group.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a group", description = "Creates a new group with the given information.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Group successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<GlobalResponseHandler<Void>> createGroup(@RequestBody @Valid GroupInfoDto groupInfoDto) {
        groupService.createGroup(groupInfoDto);
        return GlobalResponseHandler.success(ResponseStatus.GROUP_CREATED);
    }


    @Operation(summary = "Delete a group", description = "Deletes a group by its ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group successfully deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalResponseHandler<Void>> deleteGroup(@RequestBody @Valid GroupInfoDto groupInfoDto) {
        groupService.deleteGroup(groupInfoDto);
        return GlobalResponseHandler.success(ResponseStatus.GROUP_DELETED);
    }

    @Operation(summary = "Add a user to a group", description = "Adds a user to a group using the group ID and user information.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully added to group",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group or user not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            )
    })
    //TODO: jwt로 groupId 대체 예정
    @PostMapping("/add")
    public ResponseEntity<GlobalResponseHandler<Void>> addGroupUser(@RequestBody @Valid GroupUserDto groupUserDto) {
        groupService.addGroupUserById(groupUserDto);
        return GlobalResponseHandler.success(ResponseStatus.GROUP_ADD);
    }

    @Operation(summary = "Remove a user from a group", description = "Removes a user from a group using the group ID and user information.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully removed from group",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group or user not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            )
    })
    @DeleteMapping("/remove")
    public ResponseEntity<GlobalResponseHandler<Void>> removeGroupUser(@RequestBody @Valid GroupUserDto groupUserDto) {
        groupService.removeGroupUserById(groupUserDto);
        return GlobalResponseHandler.success(ResponseStatus.GROUP_REMOVE);
    }


    @Operation(summary = "Get group information for the current user", description = "Fetches the group information that the current user belongs to.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group information successfully fetched",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User or group information not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            )
    })
    //TODO: 추후 jwt기반으로 변경할 예정
    @GetMapping("/me/{id}")
    public ResponseEntity<GlobalResponseHandler<List<GroupUserInfoDto>>> getMyGroupInfo(@PathVariable Long id) {
        List<GroupUserInfoDto> groupUserInfo = groupService.getMyGroupInfoById(id);
        return GlobalResponseHandler.success(ResponseStatus.GROUP_SEARCHED, groupUserInfo);
    }
}