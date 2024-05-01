package Plant.PlantProject.controller.tradeboard;

import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.vo.request.CommentCreateRequestDto;
import Plant.PlantProject.vo.response.CommentResponseDto;
import Plant.PlantProject.service.tradeboard.CommentService;
import Plant.PlantProject.service.user.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;
    @Operation(summary = "댓글 저장", description = "중고 거래시 댓글 저장을 위해 사용하는 API")
    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody CommentCreateRequestDto commentDto) {
        CommentResponseDto comment = commentService.createComment(commentDto);
        return ResponseEntity.ok().body(comment);
    }
    @GetMapping("/comment")
    @Operation(summary = "댓글 조회", description = "중고 거래시 댓글 조회를 위해 사용하는 API")
    private ResponseEntity<Page<CommentResponseDto>> commentList(@RequestParam Long tradeBoardId, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable){
        Page<CommentResponseDto> commentDtos = commentService.pageList(tradeBoardId, pageable);
        return ResponseEntity.ok().body(commentDtos);
    }
    @GetMapping("/comment/reply")
    @Operation(summary = "대댓글 조회", description = "중고 거래시 대댓글 조회를 위해 사용하는 API")
    private ResponseEntity<List<CommentResponseDto>> replyList(@RequestParam Long tradeBoardId){
        List<CommentResponseDto> replyDtos = commentService.findCommentsByTradeBoardId(tradeBoardId);
        return ResponseEntity.ok().body(replyDtos);
    }
    @DeleteMapping("/comment/{id}")
    @Operation(summary = "댓글 삭제", description = "중고 거래시 댓글 삭제 위해 사용하는 API, 만일 대댓글이 있을 경우 연쇄 삭제")
    private ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/comment/{id}")
    @Operation(summary = "댓글 수정", description = "중고 거래시 댓글 수정을 위해 사용하는 API")
    private ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentResponseDto commentCreateRequestDto){
        CommentResponseDto commentResponseDto = commentService.updateComment(commentCreateRequestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }

}
