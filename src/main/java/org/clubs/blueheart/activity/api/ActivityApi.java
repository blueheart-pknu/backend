package org.clubs.blueheart.activity.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.clubs.blueheart.activity.application.ActivityService;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.*;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity")
public class ActivityApi {

    private final ActivityService activityService;

    public ActivityApi(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalResponseHandler<Void>> createActivity(@RequestBody @Valid ActivityCreateDto activityCreateDto) {
        activityService.createActivity(activityCreateDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalResponseHandler<Void>> updateActivity(@RequestBody @Valid ActivityUpdateDto activityUpdateDto) {
        activityService.updateActivity(activityUpdateDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UPDATED);
    }

    @GetMapping("/{status}")
    public ResponseEntity<GlobalResponseHandler<List<ActivitySearchDto>>> findActivityByStatus(@PathVariable ActivityStatus status) {
        List<ActivitySearchDto> activities = activityService.findActivityByStatus(status);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }

    //TODO: 페이지네이션 구현 알아보기
    @GetMapping("/all")
    public ResponseEntity<GlobalResponseHandler<List<ActivitySearchDto>>> findAllActivity() {
        List<ActivitySearchDto> activities = activityService.findAllActivity();
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<GlobalResponseHandler<ActivityDetailDto>> findOneActivityDetailById(@PathVariable Long id) {
        ActivityDetailDto activities = activityService.findOneActivityDetailById(id);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GlobalResponseHandler<Void>> deleteActivity(@RequestBody @Valid ActivityDeleteDto activityDeleteDto) {
        activityService.deleteActivity(activityDeleteDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_DELETED);
    }
}