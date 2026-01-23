package org.wongoo.wongoo3.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wongoo.wongoo3.global.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionhandler {

    @ExceptionHandler(WebErrorException.class)
    public ResponseEntity<ErrorResponse> handlerWebErrorException(WebErrorException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.webErrorCode, e.getMessage());

        log.warn("[WebErrorException] code = {}, message = {}", e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("요청 값이 올바르지 않습니다");

        log.warn("[ValidationException] message={}", message);

        return ResponseEntity.badRequest().body(ErrorResponse.of(WebErrorCode.INVALID_VALUE, message));
    }
}
