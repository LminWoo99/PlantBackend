package Plant.PlantProject.controller;

import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
 * 2022-02-24        이민우       특이사항 : boardContent를 엔티티 대신 dto 사용하고픔(추후 변경
 * 2022-02-28        이민우       특이사항 : boardContent를 엔티티 대신 dto 사용(테스트필수)
 * 2022-02-28        이민우       글 수정, 삭제기능 구현 (테스트 요망)
 * 2022-03-05        이민우       프론트 연동 실패(db에 값이 안들어감), @RestController로 바꿔서 json 데이터로 보내도 안돰
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class TradeBoardController {
    private final TradeBoardService tradeBoardService;
//    @GetMapping("/post")
//    public String write() {
//        System.out.println("tradeBoardService = " + tradeBoardService);
//        return "index.html";
//    }
//    @PostMapping("/post")
//        public String write(@RequestBody TradeBoardDto tradeBoardDto) {
//        System.out.println("호출");
//        tradeBoardService.saveTradePost(tradeBoardDto);
//
//            return "redirect:/post";
//        }

    @GetMapping("")
    public List<TradeBoardDto> write() {
        return tradeBoardService.findAll();
    }
    @PostMapping("")
    public ResponseEntity<TradeBoardDto> write(@RequestBody TradeBoardDto tradeBoardDto) {
        tradeBoardService.saveTradePost(tradeBoardDto);
        return ResponseEntity.ok().body(tradeBoardDto);
    }
    //    @GetMapping("/post")
//    public String write() {
//        return "indexKakao.html";
//    }
//    @PostMapping("/post")
//    public String write(@RequestBody TradeBoardDto tradeBoardDto) {
//        tradeBoardService.saveTradePost(tradeBoardDto);
//        return "redirect:/post";
//    }
// 글리스트 페이징
    @GetMapping("/list")
    public ResponseEntity<Page<TradeBoardDto>> boardList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {
        return ResponseEntity.ok(tradeBoardService.pageList(pageable));
    }
    //글 자세히보기
    @GetMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> boardContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tradeBoardService.findById(id));
    }
    //글 수정
    @PutMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> update(@PathVariable("id") Long id, @RequestBody TradeBoardDto tradeBoardDto){
        TradeBoardDto updatedTradeBoardDto=tradeBoardService.saveTradePost(tradeBoardDto);
        return ResponseEntity.ok(updatedTradeBoardDto);
    }
    //글 삭제
    @DeleteMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> delete(@PathVariable("id") Long id){
        TradeBoardDto tradeBoardDto= tradeBoardService.findById(id);
        tradeBoardService.deletePost(tradeBoardDto);
        return ResponseEntity.noContent().build();
    }

}