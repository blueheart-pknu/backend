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
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Description can only contain letters, numbers, and symbols")
    @Size(min = 1, max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotBlank(message = "Place must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Place can only contain letters, numbers, and symbols")
    @Size(min = 1, max = 100, message = "Place cannot exceed 100 characters")
    private String place;

    @NotBlank(message = "Place URL must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Place URL can only contain letters, numbers, and symbols")
    @Size(min = 1, max = 255, message = "Place URL cannot exceed 255 characters")
    private String placeUrl;

}
