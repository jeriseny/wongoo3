package org.wongoo.wongoo3.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WebErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_VALUE(HttpStatus.BAD_REQUEST),

    USER_DUPLICATE_ID(HttpStatus.BAD_REQUEST),
    USER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST),
    USER_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST),

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED);

    private HttpStatus httpStatus;
}
