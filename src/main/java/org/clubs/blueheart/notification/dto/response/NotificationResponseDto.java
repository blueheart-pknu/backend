package org.clubs.blueheart.notification.dto.response;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {

    @NotBlank(message = "Content must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Content can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    @Size(min = 1, max = 255, message = "Content cannot exceed 255 characters",
            groups = ValidationGroups.SizeGroup.class)
    private String content;

    @NotBlank(message = "Sender username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            message = "Sender username can only contain letters",
            groups = ValidationGroups.PatternGroup.class)
    private String senderUsername;

    @NotNull(message = "Creation timestamp must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private LocalDateTime createdAt;
}
