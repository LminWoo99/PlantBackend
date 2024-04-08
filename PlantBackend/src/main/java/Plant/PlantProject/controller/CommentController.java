package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.vo.CommentCreateRequestDto;
import Plant.PlantProject.dto.CommentDto;
import Plant.PlantProject.service.CommentService;
import Plant.PlantProject.service.MemberService;
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
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentCreateRequestDto commentDto) {
        CommentDto comment = commentService.createComment(commentDto);
        return ResponseEntity.ok().body(comment);
    }
    @GetMapping("/comment")
    @Operation(summary = "댓글 조회", description = "중고 거래시 댓글 조회를 위해 사용하는 API")
    private ResponseEntity<Page<CommentDto>> commentList(@RequestParam TradeBoard tradeBoardId, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable){
        Page<CommentDto> commentDtos = commentService.pageList(tradeBoardId, pageable);
        return ResponseEntity.ok().body(commentDtos);
    }
    @GetMapping("/comment/reply")
    @Operation(summary = "대댓글 조회", description = "중고 거래시 대댓글 조회를 위해 사용하는 API")
    private ResponseEntity<List<CommentDto>> replyList(@RequestParam Long tradeBoardId){
        List<CommentDto> replyDtos = commentService.findCommentsByTradeBoardId(tradeBoardId);
        return ResponseEntity.ok().body(replyDtos);
    }
    @DeleteMapping("/comment/{id}")
    @Operation(summary = "댓글 삭제", description = "중고 거래시 댓글 삭제 위해 사용하는 API, 만일 대댓글이 있을 경우 연쇄 삭제")
    private ResponseEntity<CommentDto> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/comment/{id}")
    @Operation(summary = "댓글 수정", description = "중고 거래시 댓글 수정을 위해 사용하는 API")
    private ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentCreateRequestDto){
        CommentDto commentDto = commentService.updateComment(commentCreateRequestDto);
        return ResponseEntity.ok().body(commentDto);
    }

}
