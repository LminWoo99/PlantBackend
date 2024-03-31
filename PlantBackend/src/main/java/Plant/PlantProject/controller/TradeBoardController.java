package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;

import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.*;
import Plant.PlantProject.dto.vo.ResponseTradeBoardDto;
import Plant.PlantProject.dto.vo.TradeBoardRequestDto;
import Plant.PlantProject.service.CommentService;
import Plant.PlantProject.service.MemberService;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TradeBoardController {
    private final TradeBoardService tradeBoardService;
    private final MemberService memberService;
    private final CommentService commentService;
    @PostMapping("/write")
    public ResponseEntity<ResponseTradeBoardDto> write(@RequestBody TradeBoardRequestDto tradeBoardDto) {

        Member member= memberService.getUserById(tradeBoardDto.getId());
        tradeBoardDto.setCreateBy(member.getNickname());
        tradeBoardDto.setMemberId(member.getId());
        ResponseTradeBoardDto responseTradeBoardDto = tradeBoardService.saveTradePost(tradeBoardDto);

        return ResponseEntity.ok().body(responseTradeBoardDto);
    }

    //거래게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTradeBoardDto> getTradeBoard(@PathVariable Long id) {
        ResponseTradeBoardDto tradeBoardDto = tradeBoardService.findTradeBoardById(id);
        return ResponseEntity.ok().body(tradeBoardDto);

    }
// 글리스트 페이징
    @GetMapping("/write")
    public ResponseEntity<Page<ResponseTradeBoardDto>> boardList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Page<ResponseTradeBoardDto> tradeBoardDtos = tradeBoardService.pageList(search, pageable);
        return ResponseEntity.ok(tradeBoardDtos);
    }
    //글 자세히보기
    @GetMapping("/list/{id}")
    public ResponseEntity<ResponseTradeBoardDto> boardContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tradeBoardService.findByIdx(id));
    }
    //글 수정
    @PutMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> updateTradeBoard( @PathVariable("id") Long id, @RequestBody TradeBoardDto tradeBoardDto){


        TradeBoardDto byId = tradeBoardService.findById(id);

        tradeBoardDto.setCreateBy(byId.getCreateBy());
        tradeBoardDto.setMember(byId.getMember());
        TradeBoardDto updatedTradeBoardDto=tradeBoardService.updateTradePost(tradeBoardDto);
        return ResponseEntity.ok(updatedTradeBoardDto);
    }
    //글 삭제
    @DeleteMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> deleteTradeBoard(@PathVariable("id") Long id){
        TradeBoardDto tradeBoardDto= tradeBoardService.findById(id);
        System.out.println(tradeBoardDto.getId());
        tradeBoardService.deletePost(tradeBoardDto);
        return ResponseEntity.noContent().build();
    }
    //조회수 증가
    @GetMapping("/read/{id}")
    public ResponseEntity<Integer> updateReadCount(@PathVariable Long id) {
        TradeBoardDto tradeBoardDto = tradeBoardService.findById(id);
        int view=tradeBoardService.updateView(id); // views ++

        return ResponseEntity.ok(view);
}
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<TradeBoardDto> updateStatus(@PathVariable Long id) {
        TradeBoardDto updateStatus = tradeBoardService.updateStatus(id);
        return ResponseEntity.ok().body(updateStatus);
    }
    @GetMapping("/buyer/{id}")
    public ResponseEntity<List<CommentDto>> getBuyer(@PathVariable Long id) {

        List<CommentDto> comment = commentService.findCommentsByTradeBoardId(id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/buyer/{id}")
    public ResponseEntity<ResponseTradeBoardDto> setBuyer(@PathVariable Long id, @RequestBody TradeBoardRequestDto tradeBoardDto) {

        ResponseTradeBoardDto responseTradeBoardDto = tradeBoardService.setBuyer(id, tradeBoardDto);
        return ResponseEntity.ok().body(responseTradeBoardDto);
    }


}