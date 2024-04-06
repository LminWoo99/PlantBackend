package com.example.plantsnsservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    SNS_POST_NOT_FOUND(NOT_FOUND, "해당 게시글 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글 정보를 찾을 수 없습니다.");



    private final HttpStatus httpStatus;
    private final String detail;

    public static CustomException throwSnsPostNotFound() {
        throw new CustomException(SNS_POST_NOT_FOUND);
    }
    public static CustomException throwCommentNotFound() {
        throw new CustomException(COMMENT_NOT_FOUND);
    }


}
