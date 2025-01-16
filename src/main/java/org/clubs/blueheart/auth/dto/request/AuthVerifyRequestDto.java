package org.clubs.blueheart.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthVerifyRequestDto {
    private String code;
}
