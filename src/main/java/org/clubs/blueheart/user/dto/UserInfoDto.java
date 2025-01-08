package org.clubs.blueheart.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class UserInfoDto {
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotNull(message = "Student number must not be null")
    @Min(value = 1, message = "Student number must be greater than 0")
    private Integer studentNumber;

    @NotNull(message = "User role must not be null")
    private UserRole role;
}
