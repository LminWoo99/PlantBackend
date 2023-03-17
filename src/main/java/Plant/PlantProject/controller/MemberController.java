package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
//    @GetMapping("/memberList")
//    public List<MemberDto> findAllMember(Model model){
//        List<Member> members = memberService.findAll();
//        return "members/memberList";
//    }
    //회원가입 창
    @GetMapping("/join")
    public String addForm(){
        return "memberJoin";
    }
    @PostMapping("/join")
    public String createMember(@RequestBody MemberDto memberDto) {
        memberService.joinUser(memberDto);
        return "redirect:/";
    }
    //    로그인창
    @GetMapping("/login")
    public String login() {
        return "memberLogin";
    }
    //로그인 결과

    @GetMapping("/loginResult")
    public String loginResult() {
        return "memberLoginResult";
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
    * 현재는 해당 Member에 모든 개인정보가 전달됨
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







}
