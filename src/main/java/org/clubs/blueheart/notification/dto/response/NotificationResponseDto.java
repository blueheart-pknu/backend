package org.clubs.blueheart.notification.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {
    private String content;

    private String senderUsername;

    private LocalDateTime createdAt;
}
