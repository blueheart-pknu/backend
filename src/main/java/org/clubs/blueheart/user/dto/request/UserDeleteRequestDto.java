package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeleteRequestDto {
    @NotNull(message = "UserID must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long userId;
}
