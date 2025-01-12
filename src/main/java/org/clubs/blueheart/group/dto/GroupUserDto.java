package org.clubs.blueheart.group.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUserDto {
    private Long userId;

    private Long GroupId;
}
