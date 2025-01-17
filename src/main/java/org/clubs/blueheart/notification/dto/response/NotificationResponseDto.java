package org.clubs.blueheart.notification.dto.response;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {
    @NotBlank(message = "Content must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$", message="한글, 영어, 기호, 유니코드만 사용할 수 있습니다")
    @Size(min = 1, max = 255, message = "내용은 255자 이하입니다")
    private String content;

    @NotBlank(message = "SenderUsername must not be blank")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String senderUsername;

    @NotBlank(message = "expiredAt must not be blank")
    private LocalDateTime createdAt;
}
