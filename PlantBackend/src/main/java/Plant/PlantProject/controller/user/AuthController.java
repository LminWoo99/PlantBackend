package Plant.PlantProject.controller.user;

import Plant.PlantProject.service.user.RefreshTokenService;
import Plant.PlantProject.dto.response.TokenResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService tokenService;

    @PostMapping("/token/logout")
    public void logout(@RequestHeader("Authorization") final String accessToken) {
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
    }
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(@RequestHeader("Authorization") final String accessToken) {

        String newAccessToken = tokenService.republishAccessToken(accessToken);
        if (StringUtils.hasText(newAccessToken)) {
            return ResponseEntity.ok(TokenResponseStatus.addStatus(200, newAccessToken));
        }
        return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, null));
    }

}