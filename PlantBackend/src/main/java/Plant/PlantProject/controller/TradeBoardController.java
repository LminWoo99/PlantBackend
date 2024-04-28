package Plant.PlantProject.controller;

import Plant.PlantProject.domain.Entity.Member;

import Plant.PlantProject.domain.dto.CommentDto;
import Plant.PlantProject.domain.dto.TradeBoardDto;
import Plant.PlantProject.dto.*;
import Plant.PlantProject.domain.dto.vo.ResponseTradeBoardDto;
import Plant.PlantProject.domain.dto.vo.TradeBoardRequestDto;
import Plant.PlantProject.service.tradeboard.CommentService;
import Plant.PlantProject.service.user.MemberService;
import Plant.PlantProject.service.tradeboard.TradeBoardService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "식물 중고 거래 게시글 작성", description = "식물 중고 거래 게시글 작성 할 수 있는 API")
    public ResponseEntity<ResponseTradeBoardDto> write(@RequestBody TradeBoardRequestDto tradeBoardDto) {

        Member member= memberService.getUserById(tradeBoardDto.getId());
        tradeBoardDto.setCreateBy(member.getNickname());
        tradeBoardDto.setMemberId(member.getId());
        ResponseTradeBoardDto responseTradeBoardDto = tradeBoardService.saveTradePost(tradeBoardDto);

        return ResponseEntity.ok().body(responseTradeBoardDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "식물 중고 거래 게시글 단건 조회", description = "식물 중고 거래 게시글 단건 조회 할 수 있는 API")
    public ResponseEntity<ResponseTradeBoardDto> getTradeBoard(@PathVariable Long id) {
        ResponseTradeBoardDto tradeBoardDto = tradeBoardService.findTradeBoardById(id);
        return ResponseEntity.ok().body(tradeBoardDto);

    }
// 글리스트 페이징
    @GetMapping("/write")
    @Operation(summary = "식물 중고 거래 게시글 전체 조회", description = "식물 중고 거래 게시글 전체 조회 할 수 있는 API")
    public ResponseEntity<Page<ResponseTradeBoardDto>> boardList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Page<ResponseTradeBoardDto> tradeBoardDtos = tradeBoardService.pageList(search, pageable);
        return ResponseEntity.ok(tradeBoardDtos);
    }
    @GetMapping("/list/{id}")
    @Operation(summary = "식물 중고 거래 게시글 글 자세히보기", description = "식물 중고 거래 게시글 글 자세히 볼 수 있는 API")
    public ResponseEntity<ResponseTradeBoardDto> boardContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tradeBoardService.findByIdx(id));
    }

    @PutMapping("/list/{id}")
    @Operation(summary = "식물 중고 거래 게시글 수정", description = "식물 중고 거래 게시글 수정할 수 있는 API")
    public ResponseEntity<TradeBoardDto> updateTradeBoard(@PathVariable("id") Long id, @RequestBody TradeBoardDto tradeBoardDto){


        TradeBoardDto byId = tradeBoardService.findById(id);

        tradeBoardDto.setCreateBy(byId.getCreateBy());
        tradeBoardDto.setMember(byId.getMember());
        TradeBoardDto updatedTradeBoardDto=tradeBoardService.updateTradePost(tradeBoardDto);
        return ResponseEntity.ok(updatedTradeBoardDto);
    }
    @DeleteMapping("/list/{id}")
    @Operation(summary = "식물 중고 거래 게시글 글 삭제", description = "식물 중고 거래 게시글 글 삭제할 수 있는 API, 삭제시 kafka를 통헤 관련 채팅 삭제")
    public ResponseEntity<TradeBoardDto> deleteTradeBoard(@PathVariable("id") Long id){
        TradeBoardDto tradeBoardDto= tradeBoardService.findById(id);
        System.out.println(tradeBoardDto.getId());
        tradeBoardService.deletePost(tradeBoardDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/read/{id}")
    @Operation(summary = "식물 중고 거래 게시글 조회수 증가", description = "식물 중고 거래 게시글 조회수 증가 하는 API")
    public ResponseEntity<Integer> updateReadCount(@PathVariable Long id) {
        TradeBoardDto tradeBoardDto = tradeBoardService.findById(id);
        int view=tradeBoardService.updateView(id); // views ++

        return ResponseEntity.ok(view);
}
    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "거래 완료", description = "구매자가 정해지면 거래 완료 확정 처리 하는 API")
    public ResponseEntity<TradeBoardDto> updateStatus(@PathVariable Long id) {
        TradeBoardDto updateStatus = tradeBoardService.updateStatus(id);
        return ResponseEntity.ok().body(updateStatus);
    }
    @GetMapping("/buyer/{id}")
    @Operation(summary = "구매자 조회", description = "해당 중고 게시글 구매자 조회 하는 API")
    public ResponseEntity<List<CommentDto>> getBuyer(@PathVariable Long id) {

        List<CommentDto> comment = commentService.findCommentsByTradeBoardId(id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/buyer/{id}")
    @Operation(summary = "구매자 선택", description = "채팅을 건 구매자 중에 선택 하는 API")
    public ResponseEntity<ResponseTradeBoardDto> setBuyer(@PathVariable Long id, @RequestBody TradeBoardRequestDto tradeBoardDto) {

        ResponseTradeBoardDto responseTradeBoardDto = tradeBoardService.setBuyer(id, tradeBoardDto);
        return ResponseEntity.ok().body(responseTradeBoardDto);
    }


}