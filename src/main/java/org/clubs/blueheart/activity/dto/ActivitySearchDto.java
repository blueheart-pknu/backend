package org.clubs.blueheart.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.clubs.blueheart.activity.domain.ActivityStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ActivitySearchDto {

    private Long id;

    @NotNull(message = "Title must not be null")
    private String title;

    private ActivityStatus status;

    private Boolean isSubscribed;

    private String place;

    @NotBlank(message = "CurrentNumber must not be blank")
    private Integer currentNumber;

    @NotBlank(message = "MaxNumber must not be blank")
    private Integer maxNumber;

    private LocalDateTime expiredAt;

}
