package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginRequestDto {
    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String username;

    @NotBlank(message = "StudentNumber must not be blank")
    @Pattern(regexp = "^[0-9]+$")
    private String studentNumber;
}
