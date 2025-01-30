package org.clubs.blueheart.group.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUserRequestDto {
    @NotNull(message = "UserID must not be null")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long userId;

    @NotNull(message = "GroupId must not be null")
    @Min(value = 1, message = "그룹ID는 1이상이어야합니다.")
    private Long groupId;
}
