package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.*;
import Plant.PlantProject.kakao.KaKaoService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static Plant.PlantProject.Entity.Status.판매중;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TradeBoardController {
    private final TradeBoardService tradeBoardService;
    private final MemberService memberService;
    private final CommentService commentService;
    @PostMapping("/write")
    public ResponseEntity<TradeDto> write(Principal principal, @RequestBody TradeBoardRequestDto tradeBoardDto) {

        UserDetails userDetails = (UserDetails) memberService.loadUserByUsername(principal.getName());
        System.out.println("userDetails = " + userDetails);
        Member member = memberService.getUser(userDetails.getUsername());
        tradeBoardDto.setCreateBy(member.getNickname());
        tradeBoardDto.setMemberId(member.getId());
        TradeDto tradeDto = tradeBoardService.saveTradePost(tradeBoardDto);
        System.out.println("tradeBoardDto = " + tradeBoardDto.getId());
        return ResponseEntity.ok().body(tradeDto);
    }
// 글리스트 페이징
    @GetMapping("/write")
    public ResponseEntity<Page<TradeDto>> boardList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Page<TradeDto> tradeBoardDtos = tradeBoardService.pageList(search, pageable);
        return ResponseEntity.ok(tradeBoardDtos);
    }
    //글 자세히보기
    @GetMapping("/list/{id}")
    public ResponseEntity<TradeDto> boardContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tradeBoardService.findByIdx(id));
    }
    //글 수정
    @PutMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> update(Principal principal, @PathVariable("id") Long id, @RequestBody TradeBoardDto tradeBoardDto){
        UserDetails userDetails = (UserDetails) memberService.loadUserByUsername(principal.getName());
        System.out.println("userDetails = " + userDetails);
        Member member = memberService.getUser(userDetails.getUsername());
        tradeBoardDto.setCreateBy(userDetails.getUsername());
        tradeBoardDto.setMember(member);
        TradeBoardDto updatedTradeBoardDto=tradeBoardService.updateTradePost(tradeBoardDto);
        return ResponseEntity.ok(updatedTradeBoardDto);
    }
    //글 삭제
    @DeleteMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> delete(@PathVariable("id") Long id){
        TradeBoardDto tradeBoardDto= tradeBoardService.findById(id);
        System.out.println(tradeBoardDto.getId());
        tradeBoardService.deletePost(tradeBoardDto);
        return ResponseEntity.noContent().build();
    }
    //조회수 증가
    @GetMapping("/read/{id}")
    public ResponseEntity<Integer> read(@PathVariable Long id) {
        TradeBoardDto tradeBoardDto = tradeBoardService.findById(id);
        int view=tradeBoardService.updateView(id); // views ++

        return ResponseEntity.ok(view);
}
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<TradeBoardDto> updateStatus(@PathVariable Long id, @RequestBody TradeBoardDto tradeBoardDto) {
        TradeBoardDto updateStatus = tradeBoardService.updateStatus(tradeBoardDto);
        return ResponseEntity.ok().body(updateStatus);
    }
    @GetMapping("/buyer/{id}")
    public ResponseEntity<List<CommentDto>> getBuyer(@PathVariable Long id) {

        List<CommentDto> comment = commentService.findCommentsByTradeBoardId(id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/buyer/{id}")
    public ResponseEntity<TradeDto> setBuyer(@PathVariable Long id, @RequestBody TradeBoardRequestDto tradeBoardDto) {

        TradeDto tradeDto = tradeBoardService.setBuyer(id, tradeBoardDto);
        return ResponseEntity.ok().body(tradeDto);
    }


}