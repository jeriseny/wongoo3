package org.wongoo.wongoo3.global.response;

import org.wongoo.wongoo3.global.exception.WebErrorCode;

public record ErrorResponse(String code, String message) {

    public static ErrorResponse of(WebErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.name(), message);
    }
}
