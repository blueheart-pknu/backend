package org.clubs.blueheart.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.config.ValidationGroups;

@Data
@Builder
public class UserDeleteRequestDto {
    @NotNull(message = "User ID must not be null",
            groups = ValidationGroups.NotNullGroup.class)
    @Min(value = 1, message = "User ID must be at least 1",
            groups = ValidationGroups.SizeGroup.class)
    private Long userId;
}
