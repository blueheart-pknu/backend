package org.clubs.blueheart.notification.api;

import jakarta.validation.Valid;
import org.clubs.blueheart.activity.application.ActivityHistoryService;
import org.clubs.blueheart.activity.dto.*;
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

    @PostMapping("/activity")
    public ResponseEntity<GlobalResponseHandler<Void>> notificationActivity(@RequestBody @Valid NotificationRequestDto notificationRequestDto) {
        notificationService.notificationActivity(notificationRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.NOTIFICATION_CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<GlobalResponseHandler<Void>> notificationGroup(@RequestBody @Valid NotificationRequestDto notificationRequestDto) {
        notificationService.notificationGroup(notificationRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.NOTIFICATION_CREATED);
    }

    //TODO: 추후 jwt기반으로 변경할 예정
    @GetMapping("/me/{id}")
    public ResponseEntity<GlobalResponseHandler<List<NotificationResponseDto>>> findAllNotificationMe(@PathVariable Long id) {
        List<NotificationResponseDto> notifications = notificationService.findAllNotificationMe(id);
        return GlobalResponseHandler.success(ResponseStatus.NOTIFICATION_SEARCHED, notifications);
    }
}