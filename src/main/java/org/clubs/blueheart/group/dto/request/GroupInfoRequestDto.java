package org.clubs.blueheart.group.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupInfoRequestDto {

    @NotBlank(message = "UserID must not be blank")
    @Min(value = 1, message = "사용자ID는 1이상이어야합니다.")
    private Long userId;

    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ]+$")
    private String username;

}
