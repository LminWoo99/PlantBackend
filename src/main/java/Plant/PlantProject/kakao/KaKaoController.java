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
/**
 * packageName    : Plant/PlantProject/kakao
 * fileName       : KaKaoController.java
 * author         : 이민우
 * date           : 2023-03-17
 * description    : 카카오 로그인 api controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-16        이민우       최초 생성
 * 2022-03-17        이민우       endpoint=/member/do(로그인 페이지),endpoint=/login/kakao(로그인 후페이지)
 *
 */
@RestController
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService kaKaoService;
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberService memberService;

    @GetMapping("/member/do")
    public String loginPage()
    {
        return "kakaoCI/login";
    }
    @GetMapping("/oauth2/login/kakao")
    public ResponseEntity<Map> getCI(@RequestParam String code) throws IOException {
        String access_token = kaKaoService.getToken(code);
        Map<String, Object> userInfo = kaKaoService.getUserInfo(access_token);
        UserDetails userDetails = (UserDetails) kaKaoService.loadUserByUsername((String) userInfo.get("username"));
        String jwt=jwtTokenUtil.generateAccessToken(userDetails);
        System.out.println("userInfo = " + userInfo);
        userInfo.put("access_token", jwt);
        return ResponseEntity.ok(userInfo);
    }
}
