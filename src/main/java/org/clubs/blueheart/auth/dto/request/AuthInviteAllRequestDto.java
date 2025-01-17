package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AuthInviteAllRequestDto {
    @NotBlank(message = "Creator ID must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long creatorId;
}
