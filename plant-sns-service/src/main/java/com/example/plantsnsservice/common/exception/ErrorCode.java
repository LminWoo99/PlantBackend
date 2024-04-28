package com.example.plantsnsservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    SNS_POST_NOT_FOUND(NOT_FOUND, "해당 게시글 정보를 찾을 수 없습니다.", "022"),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글 정보를 찾을 수 없습니다.", "023"),
    HASH_TAG_NOT_FOUND(NOT_FOUND, "해당 해시태그를 찾을 수 없습니다.", "024"),
    UN_SUPPORTED_FILE(UNSUPPORTED_MEDIA_TYPE, "지원하는 파일 형식이 아닙니다.", "008"),
    MAX_FILE_SIZE_EXCEEDED(PAYLOAD_TOO_LARGE, "파일 크기가 허용되는 최대치를 초과하였습니다.", "025");



    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;

    public static CustomException throwSnsPostNotFound() {
        throw new CustomException(SNS_POST_NOT_FOUND);
    }
    public static CustomException throwCommentNotFound() {
        throw new CustomException(COMMENT_NOT_FOUND);
    }
    public static CustomException throwHashTagNotFound() {
        throw new CustomException(HASH_TAG_NOT_FOUND);
    }
    public static CustomException throwFileTypeUnsupported() {
        throw new CustomException(UN_SUPPORTED_FILE);
    }
    public static CustomException throwMaxFileSizeExceeded() {
        throw new CustomException(MAX_FILE_SIZE_EXCEEDED);
    }



}
