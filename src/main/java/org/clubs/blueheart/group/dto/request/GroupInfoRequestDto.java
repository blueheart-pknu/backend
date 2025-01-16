package org.clubs.blueheart.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupInfoRequestDto {

    @NotBlank(message = "userId must not be blank")
    private Long userId;

    @NotBlank(message = "Username must not be blank")
    private String name;

}
