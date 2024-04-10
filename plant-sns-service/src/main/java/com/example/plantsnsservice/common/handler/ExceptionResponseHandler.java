package com.example.plantsnsservice.common.handler;



import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionResponseHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponse> handleCommonException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpectedException(Exception e) {
        return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getHttpStatus();
        String detail = errorCode.getDetail();

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(detail));

    }
}
