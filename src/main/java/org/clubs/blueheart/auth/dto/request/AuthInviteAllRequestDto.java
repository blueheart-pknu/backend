package org.clubs.blueheart.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor //TODO: 해결 필요
public class AuthInviteAllRequestDto {
    private Long creatorId;
}
