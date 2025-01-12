package org.clubs.blueheart.group.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

@Data
@Builder
public class GroupUserInfoDto {

    private Long id;

    @NotBlank(message = "studentNumber must not be blank")
    private String studentNumber;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "role must not be blank")
    private UserRole role;

}
