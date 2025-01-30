package org.clubs.blueheart.activity.dto.response;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.config.ValidationGroups;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ActivitySearchResponseDto {

    @NotNull(message = "Activity ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "Activity ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long activityId;

    @NotBlank(message = "Title must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Title can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    private String title;

    @NotNull(message = "Status must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private ActivityStatus status;

    @NotNull(message = "IsSubscribed must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private Boolean isSubscribed;

    @NotBlank(message = "Place must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Place can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    @Size(min = 1, max = 100, message = "Place cannot exceed 100 characters",
            groups = ValidationGroups.SizeGroup.class)
    private String place;

    @NotNull(message = "Current number must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "Current number must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    @Max(value = 999, message = "Current number cannot exceed 999",
            groups = ValidationGroups.SizeGroup.class)
    private Integer currentNumber;

    @NotNull(message = "Max number must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "Max number must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    @Max(value = 999, message = "Max number cannot exceed 999",
            groups = ValidationGroups.SizeGroup.class)
    private Integer maxNumber;

    @NotNull(message = "Expiration time must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Future(message = "Expiration time must be in the future",
            groups = ValidationGroups.DateTimeGroup.class)
    private LocalDateTime expiredAt;
}
