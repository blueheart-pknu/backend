package org.clubs.blueheart.user.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@Builder
public class UserInfoResponseDto {

    @NotBlank(message = "Username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    private String username;

    @NotNull(message = "Student number must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private String studentNumber;

    @NotNull(message = "User role must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private UserRole role;
}
