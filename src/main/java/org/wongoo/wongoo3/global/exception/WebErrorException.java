package org.wongoo.wongoo3.global.exception;

import lombok.Getter;

@Getter
public class WebErrorException extends RuntimeException {

    private final WebErrorCode errorCode;

    public WebErrorException(WebErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
