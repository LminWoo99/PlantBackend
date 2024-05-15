package Plant.PlantProject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UN_REGISTERED_EMAIL(NOT_FOUND, "해당 email로 가입된 아이디가 존재하지 않습니다", "004"),
    USER_DUPLICATED_NICKNAME(CONFLICT, "이미 가입된 닉네임입니다", "005"),
    UN_REGISTERED_ID(NOT_FOUND, "해당 아이디로 가입된 정보가 없습니다", "006"),
    GOODS_NOT_FOUND(NOT_FOUND, "해당 유저의 찜 내역을 조회 할 수 없습니다", "007"),
    UN_SUPPORTED_FILE(UNSUPPORTED_MEDIA_TYPE, "지원하는 파일 형식이 아닙니다.", "008"),
    COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 조회할 수 없습니다.",  "009"),
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원 정보를 찾을 수 없습니다.", "015"),
    TRADEBOARD_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다.", "016"),
    PLANT_NOT_FOUND(NOT_FOUND, "식물 정보를 조회할 수 없습니다", "017"),
    USER_DUPLICATED_ID(CONFLICT, "이미 가입된 아이디입니다", "018"),
    USER_DUPLICATED_EMAIL(CONFLICT, "이미 가입된 이메일입니다", "019"),
    MAX_FILE_SIZE_EXCEEDED(PAYLOAD_TOO_LARGE, "파일 크기가 허용되는 최대치를 초과하였습니다.", "025"),
    KEYWORD_NOT_FOUND(NOT_FOUND, "파일 크기가 허용되는 최대치를 초과하였습니다.", "027");


    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;
    public static CustomException throwUnRegisteredEmail() {
        throw new CustomException(UN_REGISTERED_ID);
    }
    public static CustomException throwUserDuplicatedNickname() {
        throw new CustomException(USER_DUPLICATED_NICKNAME);
    }
    public static CustomException throwUnRegisteredId() {
        throw new CustomException(UN_REGISTERED_ID);
    }
    public static CustomException throwGoodsNotFound() {
        throw new CustomException(GOODS_NOT_FOUND);
    }
    public static CustomException throwUnSupportedFile() {
        throw new CustomException(UN_SUPPORTED_FILE);
    }
    public static CustomException throwCommentNotFound() {
        throw new CustomException(COMMENT_NOT_FOUND);
    }
    public static CustomException throwMemberNotFound() {
        throw new CustomException(MEMBER_NOT_FOUND);
    }
    public static CustomException throwTradeBoardNotFound() {
        throw new CustomException(TRADEBOARD_NOT_FOUND);
    }

    public static CustomException throwPlantNotFound() {
        throw new CustomException(PLANT_NOT_FOUND);
    }
    public static CustomException throwUserDuplicatedId() {
        throw new CustomException(USER_DUPLICATED_ID);
    }

    public static CustomException throwUserDuplicatedEmail() {
        throw new CustomException(USER_DUPLICATED_EMAIL);
    }

    public static CustomException throwMaxFileSizeExceeded() {
        throw new CustomException(MAX_FILE_SIZE_EXCEEDED);
    }

    public static CustomException throwKeywordNotFound() {
        throw new CustomException(KEYWORD_NOT_FOUND);
    }

}
