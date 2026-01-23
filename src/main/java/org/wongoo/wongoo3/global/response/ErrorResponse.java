package org.wongoo.wongoo3.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wongoo.wongoo3.global.exception.WebErrorCode;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;

    public static ErrorResponse of(WebErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.name(), message);
    }
}
