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

    @GetMapping("/oauth2/login/kakao")
    public ResponseEntity<Map<String, Object>> getCI(@RequestParam String code) throws IOException {
        Map<String, Object> userInfo = kaKaoService.getToken(code);
        return ResponseEntity.ok(userInfo);
    }
}
