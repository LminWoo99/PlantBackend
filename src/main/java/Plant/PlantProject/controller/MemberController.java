package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.auth.PrincipalDetails;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * packageName    : Plant/PlantProject/controller
 * fileName       : MemberController
 * author         : 이민우
 * date           : 2023-02-24
 * description    : 일반 회원가입 , 로그인, 회원 정보 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-05        이민우       MemberController 생성
 * 2022-03-24        이민우       로그인 뷰 엔드포인트 "/loginForm", 회원가입 뷰 엔드포인트 "/joinForm"
 * 2022-03-24        이민우       로그인을 할때는 프론트단에서 데이터 요청 후 일치하면 로그인 완료, 회원가입시 백단에 데이터 받고 프론트에 넘겨줌
 * 2022-05-03        이민우       자체로그인, 구글로그인은 "loginForm",
 */
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

     BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/loginForm")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "join";
    }

    @PostMapping("/joinForm")
    public String join(@ModelAttribute MemberDto memberDto){
        memberService.joinUser(memberDto);
//        String encodePwd = bCryptPasswordEncoder.encode(member.getPassword());
//        member.setPassword(encodePwd);
//        System.out.println("encodePwd = " + encodePwd);
        return "redirect:/";
    }
    /*findInfo에서 이메일로 아이디 찾기, 아이디로 비밀번호  찾기 버튼 두개
     * 선택시 각각의 페이지로 넘어가고 결과값은 다시 loginresult에 보여줌 */
    @GetMapping("/login/findInfo")
    public String findInfo(){
        return "memberInfo";
    }
    /*
     * 회원 이메일로 아이디 조회
     * 현재는 해당 Member에 모든 개인정보가 전달됨
     * 프론트에서 dto다 전달 받더라도 아이디만 보여주자*/
    @GetMapping("/login/findInfo/id")
    public String findInfoId(){
        return "memberInfoId";
    }
    @PostMapping("/login/findInfo/id")
    public String findInfoIdResult(@RequestBody MemberDto memberDto) {
        memberService.findByUsername(memberDto.getName());
        return "redirect:/login/findInfo";
    }
    /*
     * 회원 아이디로 비밀번호 조회
     * 현재는 해당 Member에 모 findInfoPwd(든 개인정보가 전달됨
     * 프론트에서 비밀번호만 보여주자*/
    @GetMapping("/login/findInfo/pwd")
    public String findInfoPwd(){
        return "memberInfoPwd";
    }
    @PostMapping("/login/findInfo/pwd")
    public String findInfoResult(@RequestBody MemberDto memberDto){
        memberService.findByUserId(memberDto.getUserId());
        return "redirect:/memberInfo";
    }
//
//    @GetMapping("/user")
//    @ResponseBody
//    public String user(){
//        return "user";
//    }
//
//    @GetMapping("/manager")
//    @ResponseBody
//    public String manager(){
//        return "manager";
//    }
//
//    @GetMapping("/admin")
//    @ResponseBody
//    public String admin(){
//        return "admin";
//
//    }
//
//
//
//    // !!!! OAuth로 로그인 시 이 방식대로 하면 CastException 발생함
//    @GetMapping("/form/loginInfo")
//    @ResponseBody
//    public String formLoginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
//
//        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
//        Member member = principal.getMember();
//        System.out.println(member);
//
//        Member member1 = principalDetails.getMember();
//        System.out.println(member);
//
//
//        return member.toString();
//    }
//
//
//    @GetMapping("/oauth/loginInfo")
//    @ResponseBody
//    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        System.out.println(attributes);
//        // PrincipalOauth2UserService의 getAttributes내용과 같음
//
//        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();
//        // attributes == attributes1
//
//        return attributes.toString();     //세션에 담긴 user가져올 수 있음음
//    }
//
//
//    @GetMapping("/loginInfo")
//    @ResponseBody
//    public String loginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
//        String result = "";
//
//        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
//        if(principal.getMember().getProvider() == null) {
//            result = result + "Form 로그인 : " + principal;
//        }else{
//            result = result + "OAuth2 로그인 : " + principal;
//        }
//        return result;
//    }

//
//    //    로그인창
//    @GetMapping("/login")
//    public String login() {
//        return "memberLogin";
//    }
//    //로그인 결과
//
//    @GetMapping("/loginResult")
//    public String loginResult() {
//        return "memberLoginResult";
//    }

}
