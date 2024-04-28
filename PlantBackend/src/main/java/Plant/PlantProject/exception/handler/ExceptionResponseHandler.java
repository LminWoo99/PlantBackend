package Plant.PlantProject.exception.handler;




import Plant.PlantProject.exception.CustomException;
import Plant.PlantProject.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionResponseHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponse> handleCommonException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), "002"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpectedException(Exception e) {
        return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage(), "002"));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getHttpStatus();
        String detail = errorCode.getDetail();
        String errorCodeName = errorCode.getErrorCode();

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(detail, errorCodeName));

    }
}
