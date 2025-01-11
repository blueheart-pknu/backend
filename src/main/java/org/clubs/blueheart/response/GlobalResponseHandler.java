package org.clubs.blueheart.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class GlobalResponseHandler<T> {
    private int statusCode;
    private String message;
    private T data;

    /**
     * 성공 응답 생성 (데이터 포함)
     */
    public static <T> ResponseEntity<GlobalResponseHandler<T>> success(ResponseStatus status, T data) {
        return ResponseEntity.status(status.getStatusCode())
                .body(GlobalResponseHandler.<T>builder()
                        .statusCode(status.getStatusCode())
                        .message(status.getMessage())
                        .data(data)
                        .build());
    }

    /**
     * 성공 응답 생성 (데이터 없이)
     */
    public static ResponseEntity<GlobalResponseHandler<Void>> success(ResponseStatus status) {
        return ResponseEntity.status(status.getStatusCode())
                .body(GlobalResponseHandler.<Void>builder()
                        .statusCode(status.getStatusCode())
                        .message(status.getMessage())
                        .data(null)
                        .build());
    }
}