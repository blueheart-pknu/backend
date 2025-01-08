package org.clubs.blueheart.user.dto;

import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

import java.util.Optional;

@Data
@Builder
public class UserUpdateDto {
    private Long id;

    private Optional<String> username = Optional.empty();

    private Optional<String> studentNumber = Optional.empty();

    private Optional<UserRole> role = Optional.empty();
}
