package org.clubs.blueheart.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeleteRequestDto {

    private Long id;
}
