package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Comment;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.CommentCreateRequestDto;
import Plant.PlantProject.dto.CommentDto;
import Plant.PlantProject.service.CommentService;
import Plant.PlantProject.service.MemberService;
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
    @PostMapping("/comment")
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentCreateRequestDto commentDto) {
        CommentDto comment = commentService.createComment(commentDto);
        return ResponseEntity.ok().body(comment);
    }
    @GetMapping("/comment")
    private ResponseEntity<Page<CommentDto>> commentList(@RequestParam TradeBoard tradeBoardId, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable){
        Page<CommentDto> commentDtos = commentService.pageList(tradeBoardId, pageable);
        return ResponseEntity.ok().body(commentDtos);
    }
    @GetMapping("/comment/reply")
    private ResponseEntity<List<CommentDto>> replyList(@RequestParam Long tradeBoardId){
        List<CommentDto> replyDtos = commentService.findCommentsByTradeBoardId(tradeBoardId);
        return ResponseEntity.ok().body(replyDtos);
    }
    @DeleteMapping("/comment/{id}")
    private ResponseEntity<CommentDto> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/comment/{id}")
    private ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentCreateRequestDto){
        CommentDto commentDto = commentService.updateComment(commentCreateRequestDto);
        return ResponseEntity.ok().body(commentDto);
    }

}
