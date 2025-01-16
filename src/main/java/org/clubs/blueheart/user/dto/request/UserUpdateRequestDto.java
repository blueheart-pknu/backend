package org.clubs.blueheart.user.dto.request;

import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

import java.util.Optional;

@Data
@Builder
public class UserUpdateRequestDto {
    private Long id;

    private Optional<String> username = Optional.empty();

    private Optional<String> studentNumber = Optional.empty();

    private Optional<UserRole> role = Optional.empty();
}
