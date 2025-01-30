package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInviteOneRequestDto extends AuthInviteAllRequestDto {

    @NotBlank(message = "Target username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            message = "Target username can only contain letters",
            groups = ValidationGroups.PatternGroup.class)
    private String targetUsername;

    @NotBlank(message = "Target student number must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]+$",
            message = "Target student number must only contain numbers",
            groups = ValidationGroups.PatternGroup.class)
    private String targetStudentNumber;
}
