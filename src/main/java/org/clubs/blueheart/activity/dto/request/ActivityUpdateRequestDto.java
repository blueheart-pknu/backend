package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.config.ValidationGroups;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityUpdateRequestDto {

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

    @NotNull(message = "Max number must not be null",
            groups = ValidationGroups.NotNullGroup.class)

    @Size(min = 1, max = 999,
            message = "MaxNumber cannot exceed 999",
            groups = ValidationGroups.SizeGroup.class)
    private Integer maxNumber;

    @NotBlank(message = "Description must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Description can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    @Size(min = 1, max = 255, message = "Description cannot exceed 255 characters",
            groups = ValidationGroups.SizeGroup.class)
    private String description;

    @NotBlank(message = "Place must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Place can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    @Size(min = 1, max = 100, message = "Place cannot exceed 100 characters",
            groups = ValidationGroups.SizeGroup.class)
    private String place;

    @NotBlank(message = "Place URL must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Place URL can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    @Size(min = 1, max = 255, message = "Place URL cannot exceed 255 characters",
            groups = ValidationGroups.SizeGroup.class)
    private String placeUrl;

    @NotNull(message = "Expiration time must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Future(message = "Expiration time must be in the future",
            groups = ValidationGroups.DateTimeGroup.class)
    private LocalDateTime expiredAt;
}
