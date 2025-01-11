package org.clubs.blueheart.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.activity.domain.ActivityStatus;

@Data
@Builder
public class ActivityUpdateDto {

    private Long id;

    private ActivityStatus status;

    @NotBlank(message = "MaxNumber must not be blank")
    private Integer maxNumber;

    @NotNull(message = "Description must not be null")
    private String description;

    @NotNull(message = "Place must not be null")
    private String place;

    @NotNull(message = "PlaceUrl must not be null")
    private String placeUrl;
}
