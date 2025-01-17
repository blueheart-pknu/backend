package org.clubs.blueheart.activity.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ActivityDetailResponseDto extends ActivitySearchResponseDto {

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

}
