package org.clubs.blueheart.notification.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDto {

    @NotNull(message = "SenderID must not be null")
    @Min(value = 1, message = "송신자ID는 1이상이어야합니다.")
    private Long senderId;

    @NotNull(message = "ReceiverID must not be null")
    @Min(value = 1, message = "수신자ID는 1이상이어야합니다.")
    private Long receiverId;

    @NotBlank(message = "Content must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$", message="한글, 영어, 기호, 유니코드만 사용할 수 있습니다")
    @Size(min = 1, max = 255, message = "내용은 255자 이하입니다")
    private String content;
}
