package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class UserInfoRequestDto {

    @NotBlank(message = "Username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            message = "Username must only contain letters",
            groups = ValidationGroups.PatternGroup.class)
    private String username;

    @NotBlank(message = "Student number must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]+$",
            message = "Student number must contain only numbers",
            groups = ValidationGroups.PatternGroup.class)
    private String studentNumber;

    @NotNull(message = "User role must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private UserRole role;
}
