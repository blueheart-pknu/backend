package org.clubs.blueheart.group.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class GroupUserInfoResponseDto {

    @NotNull(message = "User ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "User ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long userId;

    @NotBlank(message = "Student number must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]+$",
            message = "Student number must only contain numbers",
            groups = ValidationGroups.PatternGroup.class)
    private String studentNumber;

    @NotBlank(message = "Username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            message = "Username can only contain letters",
            groups = ValidationGroups.PatternGroup.class)
    private String username;

    @NotNull(message = "Role must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    private UserRole role;
}
