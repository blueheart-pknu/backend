package org.clubs.blueheart.activity.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ActivityDetailResponseDto extends ActivitySearchResponseDto {

    private String description;

    private String place;

    private String placeUrl;

}
