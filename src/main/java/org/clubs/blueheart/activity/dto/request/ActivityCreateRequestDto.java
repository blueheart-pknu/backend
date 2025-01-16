package org.clubs.blueheart.activity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityCreateRequestDto {

    @NotNull(message = "Creator ID must not be null")
    private Long creatorId;

    @NotNull(message = "Title must not be null")
    private String title;

    @NotBlank(message = "MaxNumber must not be blank")
    private Integer maxNumber;

    @NotNull(message = "Description must not be null")
    private String description;

    @NotNull(message = "Place must not be null")
    private String place;

    @NotNull(message = "PlaceUrl must not be null")
    private String placeUrl;

    private LocalDateTime expiredAt;
}
