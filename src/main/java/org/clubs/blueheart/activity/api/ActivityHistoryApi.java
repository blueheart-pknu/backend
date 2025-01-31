package org.clubs.blueheart.activity.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.clubs.blueheart.activity.application.ActivityHistoryService;
import org.clubs.blueheart.activity.dto.request.ActivitySubscribeRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.config.ValidationSequenceConfig;
import org.clubs.blueheart.config.jwt.JwtUserDetails;
import org.clubs.blueheart.exception.CustomExceptionStatus;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-history")
public class ActivityHistoryApi {

    private final ActivityHistoryService activityHistoryService;

    public ActivityHistoryApi(ActivityHistoryService activityHistoryService) {
        this.activityHistoryService = activityHistoryService;
    }

    //TODO: 이미 내가 존재하는지 확인 필요,
    @Operation(summary = "Subscribe to an activity", description = "Subscribes the current user to an activity by providing the activity details.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully subscribed to the activity",
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
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Activity not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @PostMapping("/subscribe")
    public ResponseEntity<GlobalResponseHandler<Void>> subscribeActivity(@RequestBody @Validated(ValidationSequenceConfig.class) ActivitySubscribeRequestDto activitySubscribeRequestDto) {
        activityHistoryService.subscribeActivity(activitySubscribeRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_HISTORY_SUBSCRIBED);
    }

    @Operation(summary = "Unsubscribe from an activity", description = "Unsubscribes the current user from an activity by providing the activity details.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully unsubscribed from the activity",
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
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Activity not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @PostMapping("/unsubscribe")
    public ResponseEntity<GlobalResponseHandler<Void>> unsubscribeActivity(@RequestBody @Validated(ValidationSequenceConfig.class) ActivitySubscribeRequestDto activitySubscribeRequestDto) {
        activityHistoryService.unsubscribeActivity(activitySubscribeRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_HISTORY_UNSUBSCRIBED);
    }

    @Operation(summary = "Get My Activity History", description = "Retrieves the current user's activity subscription history.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved activity history",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @GetMapping("/me")
    public ResponseEntity<GlobalResponseHandler<List<ActivitySearchResponseDto>>> getMyActivityHistoryInfo(
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        // userDetails 에서 userId 추출
        Long userId = userDetails.getUserId();

        // DB 조회
        List<ActivitySearchResponseDto> activityHistoryInfoInfo =
                activityHistoryService.getMyActivityHistoryInfo(userId);

        // 응답
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activityHistoryInfoInfo);
    }

    // Find Subscribed Users by Activity ID API
    @Operation(summary = "Find Subscribed Users", description = "Retrieves a list of users subscribed to a specific activity.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved subscribed users",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid activity ID provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Activity not found or no subscribers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @GetMapping("/{activityId}")
    public ResponseEntity<GlobalResponseHandler<List<UserInfoResponseDto>>> findSubscribedUser(@PathVariable @Validated(ValidationSequenceConfig.class) Long activityId) {
        List<UserInfoResponseDto> users = activityHistoryService.findSubscribedUser(activityId);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, users);
    }


}