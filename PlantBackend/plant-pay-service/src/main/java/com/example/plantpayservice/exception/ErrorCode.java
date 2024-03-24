package com.example.plantchatservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원 정보를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "해당 알림을 찾을 수 없습니다." ),
    TRADEBOARD_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String detail;

    public static CustomException throwNotificationNotFound() {
        throw new CustomException(NOTIFICATION_NOT_FOUND);
    }

    public static CustomException throwMemberNotFound() {
        throw new CustomException(MEMBER_NOT_FOUND);
    }
    public static CustomException throwTradeBoardNotFound() {
        throw new CustomException(TRADEBOARD_NOT_FOUND);
    }

}
