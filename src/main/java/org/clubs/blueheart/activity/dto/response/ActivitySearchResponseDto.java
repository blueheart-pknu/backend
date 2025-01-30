package org.clubs.blueheart.activity.dto.response;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.clubs.blueheart.activity.domain.ActivityStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ActivitySearchResponseDto {

    @NotNull(message = "Activity ID must not be null")
    @Min(value = 1, message = "Activity ID must be at least 1")
    private Long activityId;

    @NotBlank(message = "Title must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Title can only contain letters, numbers, and symbols")
    private String title;

    @NotNull(message = "Status must not be null")
    private ActivityStatus status;

    @NotNull(message = "IsSubscribed must not be null")
    private Boolean isSubscribed;

    @NotBlank(message = "Place must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Place can only contain letters, numbers, and symbols")
    @Size(min = 1, max = 100, message = "Place cannot exceed 100 characters")
    private String place;

    @NotNull(message = "Current number must not be null")
    @Min(value = 1, message = "Current number must be at least 1")
    @Max(value = 999, message = "Current number cannot exceed 999")
    private Integer currentNumber;

    @NotNull(message = "Max number must not be null")
    @Min(value = 1, message = "Max number must be at least 1")
    @Max(value = 999, message = "Max number cannot exceed 999")
    private Integer maxNumber;

    @NotNull(message = "Expiration time must not be null")
    @Future(message = "Expiration time must be in the future")
    private LocalDateTime expiredAt;
}
