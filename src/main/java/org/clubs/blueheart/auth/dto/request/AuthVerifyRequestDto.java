package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;

@Builder
@Data
public class AuthVerifyRequestDto {

    @NotBlank(message = "Verification code must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\uAC00-\\uD7A3\\u0020-\\u007E]*$",
            message = "Verification code can only contain letters, numbers, and symbols",
            groups = ValidationGroups.PatternGroup.class)
    private String code;
}
