package com.example.plantsnsservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    SNS_POST_NOT_FOUND(NOT_FOUND, "해당 게시글 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글 정보를 찾을 수 없습니다."),
    HASH_TAG_NOT_FOUND(NOT_FOUND, "해당 해시태그를 찾을 수 없습니다."),
    FILE_TYPE_UNSUPPORTED(UNSUPPORTED_MEDIA_TYPE, "지원하는 파일 형식이 아닙니다.");




    private final HttpStatus httpStatus;
    private final String detail;

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
        throw new CustomException(FILE_TYPE_UNSUPPORTED);
    }


}
