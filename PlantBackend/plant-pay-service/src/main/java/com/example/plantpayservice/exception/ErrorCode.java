package com.example.plantpayservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    PAYMONEY_NOT_FOUND(NOT_FOUND, "보유한 페이머니보다 환불할 식구 페이 머니가 더 많습니다");



    private final HttpStatus httpStatus;
    private final String detail;

    public static CustomException throwPayMoneyNotFound() {
        throw new CustomException(PAYMONEY_NOT_FOUND);
    }


}
