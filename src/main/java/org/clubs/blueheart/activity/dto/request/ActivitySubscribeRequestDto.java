package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@Builder
public class ActivitySubscribeRequestDto {

    @NotNull(message = "ActivityId must not be blank",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "ActivityId must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long activityId;

    @NotNull(message = "UserId must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "User ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long userId;
}
