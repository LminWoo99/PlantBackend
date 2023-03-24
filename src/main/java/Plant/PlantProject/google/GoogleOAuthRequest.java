package Plant.PlantProject.google;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * packageName    : Plant/PlantProject/google
 * fileName       : GoogleOAuthRequest
 * author         : 이민우
 * date           : 2023-03-24
 * description    : 구글 로그인 정보 요청을 받을 모델
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-24        이민우       GoogleOAuthRequest 생성
 */
@Data
@Builder
public class GoogleOAuthRequest {
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String code;
    private String responseType;
    private String scope;
    private String accessType;
    private String grantType;
    private String state;
    private String includeGrantedScopes;
    private String loginHint;
    private String prompt;
}