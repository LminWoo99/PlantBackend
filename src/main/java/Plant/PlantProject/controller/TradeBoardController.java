package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 컨트롤러 글 작성 구현
 특이 사항: 프론트 협업시 글작성 api url은 "/post"
*/
/**
 * packageName    : Plant/PlantProject/controller
 * fileName       : TradeBoardController
 * author         : 이민우
 * date           : 2023-02-24
 * description    : 거래 게시글 컨트롤러 글 작성 구현
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-02-23        이민우       최초 생성
 * 2022-02-24        이민우       게시글 페이징
 * 2022-02-24        이민우       특이사항 : boardContent를 엔티티 대신 dto 사용하고픔(추후 변경)
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
// 글리스트 페이징
    @GetMapping("/post/list")
    public String boardList(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable) {
        model.addAttribute("tradeBoardDto", tradeBoardService.pageList(pageable));
        return "/post/list";
    }
//
@GetMapping("/post/list/{id}")
public String boardContent(@PathVariable("id") Long id, Model model) {
    TradeBoard tradeBoard = tradeBoardService.findById(id);
    model.addAttribute(tradeBoard);

    return "/post/boardContent";
}

}
