package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityDeleteRequestDto {

    @NotBlank(message = "ActivityId must not be blank")
    @Min(value = 1, message = "ActivityId는 1이상이어야합니다.")
    private Long activityId;
}
