package org.clubs.blueheart.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthVerifyDto {
    private String code;
}
