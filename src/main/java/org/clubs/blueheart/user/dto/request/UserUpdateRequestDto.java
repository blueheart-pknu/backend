package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

import java.util.Optional;

@Data
@Builder
public class UserUpdateRequestDto {
    @NotNull(message = "UserId must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long userId;

    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String username;

    @Pattern(regexp = "^[0-9]+$")
    private String studentNumber;

    private UserRole role;
}
