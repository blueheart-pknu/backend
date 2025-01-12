package org.clubs.blueheart.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor //TODO: 해결 필요
public class AuthInviteOneDto extends AuthInviteAllDto{

    private String targetUsername;

    private String targetStudentNumber;
}
