package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInviteOneRequestDto extends AuthInviteAllRequestDto {

    @NotBlank(message = "TargetUsername must not be blank")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String targetUsername;

    @NotBlank(message = "TargetStudentNumber must not be blank")
    @Pattern(regexp = "^[0-9]+$")
    private String targetStudentNumber;
}
