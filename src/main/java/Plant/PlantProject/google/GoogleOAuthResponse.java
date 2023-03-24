package Plant.PlantProject.google;

import lombok.Data;
/**
 * packageName    : Plant/PlantProject/google
 * fileName       : GoogleOAuthResponse
 * author         : 이민우
 * date           : 2023-03-24
 * description    : 구글 로그인 정보 응답을 받을 모델
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-24        이민우       GoogleOAuthResponse 생성
 */
@Data
public class GoogleOAuthResponse {
	
	private String accessToken;
	private String expiresIn;
	private String refreshToken;
	private String scope;
	private String tokenType;
	private String idToken;
	
	
};