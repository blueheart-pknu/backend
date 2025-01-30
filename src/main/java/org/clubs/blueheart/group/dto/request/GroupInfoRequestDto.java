package org.clubs.blueheart.group.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@Builder
public class GroupInfoRequestDto {

    @NotNull(message = "User ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "User ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long userId;

    @NotBlank(message = "Username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            message = "Username can only contain letters",
            groups = ValidationGroups.PatternGroup.class)
    private String username;
}
