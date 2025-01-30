package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class UserUpdateRequestDto {

    @NotNull(message = "User ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "User ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long userId;

    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            message = "Username must only contain letters",
            groups = ValidationGroups.PatternGroup.class)
    private String username;

    @Pattern(regexp = "^[0-9]+$",
            message = "Student number must contain only numbers",
            groups = ValidationGroups.PatternGroup.class)
    private String studentNumber;

    @NotNull(message = "Role must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private UserRole role;
}
