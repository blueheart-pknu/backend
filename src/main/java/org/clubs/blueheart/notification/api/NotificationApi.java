package org.clubs.blueheart.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.clubs.blueheart.notification.application.NotificationService;
import org.clubs.blueheart.notification.dto.NotificationRequestDto;
import org.clubs.blueheart.notification.dto.NotificationResponseDto;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationApi {

    private final NotificationService notificationService;

    public NotificationApi(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //TODO: 자신 빼고 브로드캐스트 할 수 있게 변경
    @Operation(summary = "Send notification for activity", description = "Sends a notification to all participants of the specified activity.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Notification successfully sent",
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
    @PostMapping("/activity")
    public ResponseEntity<GlobalResponseHandler<Void>> notificationActivity(@RequestBody @Valid NotificationRequestDto notificationRequestDto) {
        notificationService.notificationActivity(notificationRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.NOTIFICATION_CREATED);
    }

    //TODO: 자신 빼고 브로드캐스트 할 수 있게 변경
    @Operation(summary = "Send notification for group", description = "Sends a notification to all members of the specified group.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Notification successfully sent",
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
    @PostMapping("/group")
    public ResponseEntity<GlobalResponseHandler<Void>> notificationGroup(@RequestBody @Valid NotificationRequestDto notificationRequestDto) {
        notificationService.notificationGroup(notificationRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.NOTIFICATION_CREATED);
    }

    @Operation(summary = "Get all notifications for the current user", description = "Fetches all notifications for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notifications successfully fetched",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No notifications found for the specified user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponseHandler.class)
                    )
            )
    })
    @GetMapping("/me/{id}")
    public ResponseEntity<GlobalResponseHandler<List<NotificationResponseDto>>> findAllNotificationMe(@PathVariable Long id) {
        List<NotificationResponseDto> notifications = notificationService.findAllNotificationMe(id);
        return GlobalResponseHandler.success(ResponseStatus.NOTIFICATION_SEARCHED, notifications);
    }
}