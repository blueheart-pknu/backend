package org.clubs.blueheart.activity.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.clubs.blueheart.activity.application.ActivityHistoryService;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.*;
import org.clubs.blueheart.exception.CustomExceptionStatus;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-history")
public class ActivityHistoryApi {

    private final ActivityHistoryService activityHistoryService;

    public ActivityHistoryApi(ActivityHistoryService activityHistoryService) {
        this.activityHistoryService = activityHistoryService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<GlobalResponseHandler<Void>> subscribeActivity(@RequestBody @Valid ActivitySubscribeDto activitySubscribeDto) {
        activityHistoryService.subscribeActivity(activitySubscribeDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SUBSCRIBED);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<GlobalResponseHandler<Void>> unsubscribeActivity(@RequestBody @Valid ActivitySubscribeDto activitySubscribeDto) {
        activityHistoryService.unsubscribeActivity(activitySubscribeDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UNSUBSCRIBED);
    }
}