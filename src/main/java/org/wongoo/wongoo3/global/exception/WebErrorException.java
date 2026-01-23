package org.wongoo.wongoo3.global.exception;

public class WebErrorException extends RuntimeException {

    WebErrorCode webErrorCode;

    public WebErrorException(WebErrorCode webErrorCode, String customMessage) {
        super(customMessage);
        this.webErrorCode = webErrorCode;
    }

    public WebErrorCode getErrorCode() {
        return this.webErrorCode;
    }
}
