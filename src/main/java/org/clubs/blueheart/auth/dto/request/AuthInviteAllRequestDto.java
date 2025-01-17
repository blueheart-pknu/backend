package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInviteAllRequestDto {
    @NotNull(message = "Creator ID must not be blank")
    @Min(value = 1)
    private Long creatorId;
}
