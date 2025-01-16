package org.clubs.blueheart.auth.dto;

import lombok.Builder;
import lombok.Data;
import org.clubs.blueheart.user.domain.UserRole;

//TODO: 추후에 VO로 변형도 생각해보기
@Builder
@Data
public class AuthJwtDto {

    private Long id;

    private String studentNumber;

    private String username;

    private UserRole role;

}
