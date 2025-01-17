package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivitySubscribeRequestDto {

    @NotNull(message = "ActivityId must not be blank")
    @Min(value = 1, message = "액티비티ID는 1이상이어야합니다.")
    private Long activityId;

    @NotNull(message = "UserId must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long userId;
}
