package org.clubs.blueheart.activity.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.clubs.blueheart.activity.application.ActivityService;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.request.ActivityCreateRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityDeleteRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityUpdateRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivityDetailResponseDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.config.ValidationSequenceConfig;
import org.clubs.blueheart.exception.CustomExceptionStatus;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity")
public class ActivityApi {

    private final ActivityService activityService;

    public ActivityApi(ActivityService activityService) {
        this.activityService = activityService;
    }


    @Operation(summary = "액티비티 생성", description = "새로운 액티비티를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "액티비티 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<GlobalResponseHandler<Void>> createActivity(@RequestBody @Validated(ValidationSequenceConfig.class) ActivityCreateRequestDto activityCreateRequestDto) {
        activityService.createActivity(activityCreateRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_CREATED);
    }

    @Operation(summary = "액티비티 업데이트", description = "기존 액티비티를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "액티비티 업데이트 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "액티비티를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<GlobalResponseHandler<Void>> updateActivity(@RequestBody @Validated(ValidationSequenceConfig.class) ActivityUpdateRequestDto activityUpdateRequestDto) {
        activityService.updateActivity(activityUpdateRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_UPDATED);
    }

    @Operation(summary = "액티비티 상태별 조회", description = "특정 상태의 액티비티 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "액티비티 상태별 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 상태 입력",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @GetMapping("/{status}")
    public ResponseEntity<GlobalResponseHandler<List<ActivitySearchResponseDto>>> findActivityByStatus(@PathVariable ActivityStatus status) {
        List<ActivitySearchResponseDto> activities = activityService.findActivityByStatus(status);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }

    @Operation(summary = "모든 액티비티 조회", description = "삭제되지 않은 모든 액티비티 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모든 액티비티 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            )
    })
    //TODO: 페이지네이션 구현 알아보기
    @GetMapping("/all")
    public ResponseEntity<GlobalResponseHandler<List<ActivitySearchResponseDto>>> findAllActivity() {
        List<ActivitySearchResponseDto> activities = activityService.findAllActivity();
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }
    @Operation(summary = "액티비티 상세 조회", description = "특정 ID를 가진 액티비티의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "액티비티 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "액티비티를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @GetMapping("/detail/{id}")
    public ResponseEntity<GlobalResponseHandler<ActivityDetailResponseDto>> findOneActivityDetailById(@PathVariable Long id) {
        ActivityDetailResponseDto activities = activityService.findOneActivityDetailById(id);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_SEARCHED, activities);
    }

    @Operation(summary = "액티비티 삭제", description = "특정 액티비티를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "액티비티 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "액티비티를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalResponseHandler<Void>> deleteActivity(@RequestBody @Validated(ValidationSequenceConfig.class) ActivityDeleteRequestDto activityDeleteRequestDto) {
        activityService.deleteActivity(activityDeleteRequestDto);
        return GlobalResponseHandler.success(ResponseStatus.ACTIVITY_DELETED);
    }
}