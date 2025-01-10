package org.clubs.blueheart.notification.api;

import jakarta.validation.Valid;
import org.clubs.blueheart.activity.application.ActivityHistoryService;
import org.clubs.blueheart.activity.dto.*;
import org.clubs.blueheart.notification.application.NotificationService;
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
    public ResponseEntity<GlobalResponseHandler<Void>> notificationActivity(@RequestBody @Valid ActivitySubscribeDto activitySubscribeDto) {
        notificationService.subscribeActivity(activitySubscribeDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SUBSCRIBED);
    }

    @PostMapping("/group")
    public ResponseEntity<GlobalResponseHandler<Void>> notificationGroup(@RequestBody @Valid ActivitySubscribeDto activitySubscribeDto) {
        notificationService.unsubscribeActivity(activitySubscribeDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UNSUBSCRIBED);
    }

    //TODO: 추후 jwt기반으로 변경할 예정
    @GetMapping("/me/{id}")
    public ResponseEntity<GlobalResponseHandler<List<ActivitySearchDto>>> findNotificationAll(@PathVariable Long id) {
        List<ActivitySearchDto> activities = notificationService.findOneActivityDetailById(id);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }
}