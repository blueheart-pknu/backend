package org.clubs.blueheart.group.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class GroupUserInfoResponseDto {

    @NotNull(message = "UserId must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long userId;

    @NotBlank(message = "studentNumber must not be blank")
    @Pattern(regexp = "^[0-9]+$")
    private String studentNumber;

    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String username;

    @NotBlank(message = "role must not be blank")
    @Pattern(regexp = "^[A-Z]+$")
    private UserRole role;

}
