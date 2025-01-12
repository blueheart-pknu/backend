package org.clubs.blueheart.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor //TODO: 해결 필요
public class AuthInviteAllDto {
    private Long creatorId;
}
