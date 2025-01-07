package org.clubs.blueheart.user.dto;

import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class UserInfoDto {
    private String username;

    private Integer studentNumber;

    private UserRole role;
}
