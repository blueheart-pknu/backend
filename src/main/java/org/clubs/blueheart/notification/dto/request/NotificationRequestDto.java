package org.clubs.blueheart.notification.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@Builder
public class NotificationRequestDto {

    @NotNull(message = "Sender ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "Sender ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long senderId;

    @NotNull(message = "Receiver ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "Receiver ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long receiverId;

    @NotBlank(message = "Content must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Content can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    @Size(min = 1, max = 255, message = "Content cannot exceed 255 characters",
            groups = ValidationGroups.SizeGroup.class)
    private String content;
}
