package org.clubs.blueheart.auth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInviteAllRequestDto {
    @NotNull(message = "Creator ID must not be blank",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1,
            message = "creatorId must be at least 1",
    groups = ValidationGroups.SizeGroup.class)
    private Long creatorId;
}
