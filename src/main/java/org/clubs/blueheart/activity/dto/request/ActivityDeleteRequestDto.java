package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityDeleteRequestDto {

    @NotBlank(message = "ID must not be blank")
    @Min(value = 1, message = "ID는 1이상이어야합니다.")
    private Long id;
}
