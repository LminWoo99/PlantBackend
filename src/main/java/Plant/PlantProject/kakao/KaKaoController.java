package Plant.PlantProject.kakao;

import Plant.PlantProject.config.JwtTokenUtil;
import Plant.PlantProject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
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
        String accessToken=jwtTokenUtil.generateAccessToken(userDetails);
        String refreshToken=jwtTokenUtil.generateRefreshToken(userDetails);
        System.out.println("userInfo = " + userInfo);
        userInfo.put("access_token", accessToken);
        userInfo.put("refresh_token", refreshToken);
        return ResponseEntity.ok(userInfo);
    }
}
