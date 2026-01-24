package org.wongoo.wongoo3.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WebErrorCode {
    CONFLICT(HttpStatus.CONFLICT),
    BAD_REQUEST(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;
}
