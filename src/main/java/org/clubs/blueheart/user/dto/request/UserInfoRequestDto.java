package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class UserInfoRequestDto {
    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String username;

    @NotBlank(message = "studentNumber must not be blank")
    @Pattern(regexp = "^[0-9]+$")
    private String studentNumber;

    @NotBlank(message = "role must not be blank")
    @Pattern(regexp = "^[A-Z]+$")
    private UserRole role;
}
