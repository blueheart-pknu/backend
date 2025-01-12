package org.clubs.blueheart.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDto {

    private Long senderId;

    private Long receiverId;

    private String content;
}
