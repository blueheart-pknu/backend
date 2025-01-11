package org.clubs.blueheart.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum ResponseStatus {

    // USER RESPONSE CODE
    USER_SEARCHED(HttpStatus.OK, "유저를 성공적으로 조회했습니다"),
    USER_CREATED(HttpStatus.CREATED, "유저가 성공적으로 생성되었습니다"),
    USER_DELETED(HttpStatus.OK, "유저가 성공적으로 삭제되었습니다"),
    USER_UPDATED(HttpStatus.OK, "유저 정보가 성공적으로 갱신되었습니다"),

    // ACTIVITY RESPONSE CODE
    ACTIVITY_CREATED(HttpStatus.CREATED, "액티비티가 성공적으로 생성되었습니다"),
    ACTIVITY_UPDATED(HttpStatus.OK, "액티비티 정보가 성공적으로 갱신되었습니다"),
    ACTIVITY_DELETED(HttpStatus.OK, "액티비티를 성공적으로 삭제했습니다"),
    ACTIVITY_SEARCHED(HttpStatus.OK, "액티비티를 성공적으로 조회했습니다"),
    ACTIVITY_SUBSCRIBED(HttpStatus.OK, "액티비티를 성공적으로 구독신청했습니다"),
    ACTIVITY_UNSUBSCRIBED(HttpStatus.OK, "액티비티를 성공적으로 구독해지했습니다");


    // AUTH RESPONSE CODE

    // GROUP RESPONSE CODE

    // NOTIFICATION RESPONSE CODE


    private final int statusCode;
    private final String message;

    ResponseStatus(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.message = message;
    }

}

