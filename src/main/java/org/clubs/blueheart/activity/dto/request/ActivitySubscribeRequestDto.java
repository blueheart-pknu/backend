package org.clubs.blueheart.activity.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivitySubscribeRequestDto {

    private Long activityId;

    private Long userId;
}
