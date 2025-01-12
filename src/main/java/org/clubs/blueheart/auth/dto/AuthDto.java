package org.clubs.blueheart.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotNull(message = "Student number must not be null")
    private String studentNumber;
}
