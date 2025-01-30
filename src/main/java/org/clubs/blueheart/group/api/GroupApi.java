package org.clubs.blueheart.group.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.clubs.blueheart.config.ValidationSequenceConfig;
import org.clubs.blueheart.config.jwt.JwtUserDetails;
import org.clubs.blueheart.group.application.GroupService;
import org.clubs.blueheart.group.dto.request.GroupUserRequestDto;
import org.clubs.blueheart.group.dto.request.GroupInfoRequestDto;
import org.clubs.blueheart.group.dto.response.GroupUserInfoResponseDto;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<GlobalResponseHandler<Void>> createGroup(@RequestBody @Validated(ValidationSequenceConfig.class) GroupInfoRequestDto groupInfoRequestDto) {
        groupService.createGroup(groupInfoRequestDto);
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
    public ResponseEntity<GlobalResponseHandler<Void>> deleteGroup(@RequestBody @Validated(ValidationSequenceConfig.class) GroupInfoRequestDto groupInfoRequestDto) {
        groupService.deleteGroup(groupInfoRequestDto);
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
    public ResponseEntity<GlobalResponseHandler<Void>> addGroupUser(@RequestBody @Validated(ValidationSequenceConfig.class) GroupUserRequestDto groupUserRequestDto) {
        groupService.addGroupUserById(groupUserRequestDto);
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
    public ResponseEntity<GlobalResponseHandler<Void>> removeGroupUser(@RequestBody @Validated(ValidationSequenceConfig.class) GroupUserRequestDto groupUserRequestDto) {
        groupService.removeGroupUserById(groupUserRequestDto);
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
    @GetMapping("/me")
    public ResponseEntity<GlobalResponseHandler<List<GroupUserInfoResponseDto>>> getMyGroupInfo(
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        // 1) JWT 필터에서 검증된 userDetails를 통해 userId 추출
        Long userId = userDetails.getUserId();

        System.out.println(userId);

        // 2) 기존 로직: userId 기반으로 DB 조회
        List<GroupUserInfoResponseDto> groupUserInfo = groupService.getMyGroupInfoByUserId(userId);

        // 3) 응답
        return GlobalResponseHandler.success(ResponseStatus.GROUP_SEARCHED, groupUserInfo);
    }
}