package org.clubs.blueheart.auth.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;
import org.clubs.blueheart.user.domain.UserRole;

//TODO: 추후에 VO로 변형도 생각해보기
@Builder
@Data
public class AuthJwtResponseDto {

    @Min(value = 1,
            groups = ValidationGroups.SizeGroup.class)
    private Long id;

    @NotBlank(message = "StudentNumber must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]+$",
            groups = ValidationGroups.PatternGroup.class)
    private String studentNumber;

    @NotBlank(message = "Username must not be blank",
            groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$",
            groups = ValidationGroups.PatternGroup.class)
    private String username;


    private UserRole role;

}
