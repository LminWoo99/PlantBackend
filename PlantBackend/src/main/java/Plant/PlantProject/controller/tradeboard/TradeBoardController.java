package Plant.PlantProject.controller.tradeboard;

import Plant.PlantProject.vo.response.CommentResponseDto;
import Plant.PlantProject.vo.request.TradeBoardRequestDto;
import Plant.PlantProject.vo.response.TradeBoardResponseDto;
import Plant.PlantProject.service.tradeboard.CommentService;
import Plant.PlantProject.service.tradeboard.TradeBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeBoardController {
    private final TradeBoardService tradeBoardService;
    private final CommentService commentService;
    @PostMapping("/trade-board")
    @Operation(summary = "식물 중고 거래 게시글 작성", description = "식물 중고 거래 게시글 작성 할 수 있는 API")
    public ResponseEntity<Long> write(@RequestPart TradeBoardRequestDto tradeBoardDto, @RequestPart("file") List<MultipartFile> files) throws IOException {

        Long id=tradeBoardService.saveTradePost(tradeBoardDto, files);

        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "식물 중고 거래 게시글 단건 조회", description = "식물 중고 거래 게시글 단건 조회 할 수 있는 API")
    public ResponseEntity<TradeBoardResponseDto> getTradeBoard(@PathVariable Long id) {
        TradeBoardResponseDto tradeBoardDto = tradeBoardService.findById(id);
        return ResponseEntity.ok().body(tradeBoardDto);

    }
    @GetMapping("/list")
    @Operation(summary = "식물 중고 거래 게시글 전체 조회", description = "식물 중고 거래 게시글 전체 조회 할 수 있는 API")
    public ResponseEntity<Page<TradeBoardResponseDto>> boardList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Page<TradeBoardResponseDto> tradeBoardDtos = tradeBoardService.pageList(search, pageable);
        return ResponseEntity.ok(tradeBoardDtos);
    }
    @GetMapping("/list/{id}")
    @Operation(summary = "식물 중고 거래 게시글 글 자세히보기", description = "식물 중고 거래 게시글 글 자세히 볼 수 있는 API")
    public ResponseEntity<TradeBoardResponseDto> boardContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tradeBoardService.findById(id));
    }

    @PutMapping("/list/{id}")
    @Operation(summary = "식물 중고 거래 게시글 수정", description = "식물 중고 거래 게시글 수정할 수 있는 API")
    public ResponseEntity<Long> updateTradeBoard(@PathVariable("id") Long id, @RequestBody TradeBoardRequestDto tradeBoardRequestDto){
        Long tradeBoardId = tradeBoardService.updateTradePost(id, tradeBoardRequestDto);
        return ResponseEntity.ok().body(tradeBoardId);
    }
    @DeleteMapping("/list/{id}")
    @Operation(summary = "식물 중고 거래 게시글 글 삭제", description = "식물 중고 거래 게시글 글 삭제할 수 있는 API, 삭제시 kafka를 통헤 관련 채팅 삭제")
    public ResponseEntity<TradeBoardRequestDto> deleteTradeBoard(@PathVariable("id") Long id){
        tradeBoardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/view/{id}")
    @Operation(summary = "식물 중고 거래 게시글 조회수 증가", description = "식물 중고 거래 게시글 조회수 증가 하는 API")
    public ResponseEntity<Integer> updateReadCount(@PathVariable Long id) {
        Integer view = tradeBoardService.updateView(id);// views ++

        return ResponseEntity.ok(view);
}
    @PutMapping("/update-status/{id}")
    @Operation(summary = "거래 완료", description = "구매자가 정해지면 거래 완료 확정 처리 하는 API")
    public ResponseEntity<Long> updateStatus(@PathVariable Long id) {
        Long tradeBoardId = tradeBoardService.updateStatus(id);
        return ResponseEntity.ok().body(tradeBoardId);
    }
    @GetMapping("/buyer/{id}")
    @Operation(summary = "구매자 조회", description = "해당 중고 게시글 구매자 조회 하는 API")
    public ResponseEntity<List<CommentResponseDto>> getBuyer(@PathVariable Long id) {

        List<CommentResponseDto> comment = commentService.findCommentsByTradeBoardId(id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/buyer/{id}")
    @Operation(summary = "구매자 선택", description = "채팅을 건 구매자 중에 선택 하는 API")
    public ResponseEntity<TradeBoardResponseDto> setBuyer(@PathVariable Long id, @RequestBody TradeBoardRequestDto tradeBoardDto) {

        TradeBoardResponseDto tradeBoardResponseDto = tradeBoardService.setBuyer(id, tradeBoardDto);
        return ResponseEntity.ok().body(tradeBoardResponseDto);
    }
    @GetMapping("/tradeInfo/{id}")
    @Operation(summary = "유저 기능 -거래 정보 조회", description = "유저 기능중 개인이 거래한 내역을 확인 할 수 있는 API")
    public ResponseEntity<List<TradeBoardResponseDto>> showTradeInfo(@PathVariable Long id){
        List<TradeBoardResponseDto> tradeBoardResponseDtos = tradeBoardService.showTradeInfo(id);
        return ResponseEntity.ok().body(tradeBoardResponseDtos);
    }
    @GetMapping("/buyInfo/{id}")
    @Operation(summary = "유저 기능 -구매 정보 조회", description = "유저 기능중 개인이 구매한 내역을 확인 할 수 있는 API")
    public ResponseEntity<List<TradeBoardResponseDto>> showBuyInfo(@PathVariable Long id){
        List<TradeBoardResponseDto> tradeBoardResponseDtos = tradeBoardService.showBuyInfo(id);
        return ResponseEntity.ok().body(tradeBoardResponseDtos);
    }


}