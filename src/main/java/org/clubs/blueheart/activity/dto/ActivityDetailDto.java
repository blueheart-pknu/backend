package org.clubs.blueheart.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.clubs.blueheart.activity.domain.ActivityStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ActivityDetailDto extends ActivitySearchDto {

    private String description;

    private String place;

    private String placeUrl;

}
