package com.example.plantpayservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */

    INSUFFICIENT_PAYMONEY(BAD_REQUEST, "거레할 금액보다 구매자 보유 페이머니가 적습니다", "011"),
    COUPON_NOT_FOUND(NOT_FOUND, "해당 쿠폰 정보를 찾을수 없습니다", "012"),
    INSUFFICIENT_REFUND_PAYMONEY(BAD_REQUEST, "보유한 페이머니보다 환불할 식구 페이 머니가 더 많습니다", "021");



    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;


    public static CustomException throwInsufficientRefundPayMoney() {
        throw new CustomException(INSUFFICIENT_REFUND_PAYMONEY);
    }
    public static CustomException throwInsufficientPayMoney() {
        throw new CustomException(INSUFFICIENT_PAYMONEY);
    }
    public static CustomException couponNotFound() {
        throw new CustomException(
                COUPON_NOT_FOUND);
    }


}
