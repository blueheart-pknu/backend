package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.activity.domain.ActivityStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityUpdateRequestDto {

    @NotBlank(message = "Creator ID must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long creatorId;

    @NotBlank(message = "Title must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$")
    private String title;

    @NotBlank(message = "Status must not be blank")
    @Pattern(regexp = "^[A-Z]*$")
    private ActivityStatus status;

    @NotBlank(message = "MaxNumber must not be blank")
    @Size(min = 1, max = 999, message = "최대값은 999이하입니다")
    private Integer maxNumber;

    @NotBlank(message = "Description must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$", message="한글, 영어, 기호, 유니코드만 사용할 수 있습니다")
    @Size(min = 1, max = 255, message = "내용은 255자 이하입니다")
    private String description;

    @NotBlank(message = "Place must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$", message="한글, 영어, 기호, 유니코드만 사용할 수 있습니다")
    @Size(min = 1, max = 100, message = "내용은 100자 이하입니다")
    private String place;

    @NotBlank(message = "PlaceUrl must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$", message="한글, 영어, 기호, 유니코드만 사용할 수 있습니다")
    @Size(min = 1, max = 255, message = "내용은 255자 이하입니다")
    private String placeUrl;

    @NotBlank(message = "expiredAt must not be blank")
    @Future(message = "만료시각은 현재보다 뒤여야 합니다.")
    private LocalDateTime expiredAt;
}
