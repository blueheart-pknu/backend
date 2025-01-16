package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class UserInfoRequestDto {
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotNull(message = "Student number must not be null")
    private String studentNumber;

    @NotNull(message = "User role must not be null")
    private UserRole role;
}
