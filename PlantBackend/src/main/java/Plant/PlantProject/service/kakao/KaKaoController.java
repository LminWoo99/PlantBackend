package Plant.PlantProject.service.kakao;

import Plant.PlantProject.common.config.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService kaKaoService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/oauth2/login/kakao")
    public ResponseEntity<Map> getCI(@RequestParam String code) throws IOException {
        Map<String, Object> token = kaKaoService.getToken(code);
        Map<String, Object> userInfo = kaKaoService.getUserInfo(token);
        UserDetails userDetails = (UserDetails) kaKaoService.loadUserByUsername((String) userInfo.get("username"));
        String accessToken=jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken=jwtTokenUtil.generateRefreshToken(userDetails.getUsername());
        System.out.println("userInfo = " + userInfo);
        userInfo.put("access_token", accessToken);
        userInfo.put("refresh_token", refreshToken);
        return ResponseEntity.ok(userInfo);
    }
}
