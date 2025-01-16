package org.clubs.blueheart.group.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUserRequestDto {
    private Long userId;

    private Long GroupId;
}
