package Plant.PlantProject.kakao;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
@Controller
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService kaKaoService;

    @GetMapping("/member/do")
    public String loginPage()
    {
        return "kakaoCI/login";
    }
    @GetMapping("/login/kakao")
    public String getCI(@RequestParam String code, Model model) throws IOException {
        System.out.println("code = " + code);
        String access_token = kaKaoService.getToken(code);
        Map<String, Object> userInfo = kaKaoService.getUserInfo(access_token);
        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);

        return "indexKakao";
    }
}
