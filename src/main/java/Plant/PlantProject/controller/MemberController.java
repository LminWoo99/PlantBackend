package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
//    @GetMapping("/memberList")
//    public List<MemberDto> findAllMember(Model model){
//        List<Member> members = memberService.findAll();
//        model.addAttribute("members", members);
//        return "members/memberList";
//    }




}
