package Plant.PlantProject.controller;

import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.repository.TradeBoardRepository;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 컨트롤러 글 작성 구현
 특이 사항: 프론트 협업시 글작성 api url은 "/post"
*/
@Controller
@RequiredArgsConstructor
public class TradeBoardController {
    private final TradeBoardService tradeBoardService;
//    글작성 폼 호출
    @GetMapping("/post")
    public String write() {
        return "/post";
    }
    @PostMapping("/post")
    public String write(TradeBoardDto tradeBoardDto) {
        tradeBoardService.saveTradePost(tradeBoardDto);
        return "redirect:/";
    }
}
