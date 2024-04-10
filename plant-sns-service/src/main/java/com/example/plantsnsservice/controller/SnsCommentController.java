package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.service.SnsCommentService;
import com.example.plantsnsservice.vo.request.SnsCommentRequestDto;
import com.example.plantsnsservice.vo.response.SnsCommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SnsCommentController {
    private final SnsCommentService snsCommentService;

    @PostMapping("/snsComment")
    @Operation(summary = "댓글 저장", description = "SNS 게시글에 댓글달 수 있는 API")
    public ResponseEntity<SnsCommentResponseDto> createSnsComment(@RequestBody SnsCommentRequestDto snsCommentRequestDto) {
        SnsCommentResponseDto snsCommentResponseDto = snsCommentService.createComment(snsCommentRequestDto);
        return ResponseEntity.ok().body(snsCommentResponseDto);
    }

    @GetMapping("/snsComment/{snsPostId}")
    @Operation(summary = "댓글 , 대댓글 조회", description = "SNS 게시글 댓글 , 대댓글 조회할 수 있는 API")
    public ResponseEntity<List<SnsCommentResponseDto>> getSnsComment(@PathVariable Long snsPostId) {
        List<SnsCommentResponseDto> snsCommentResponseDtoList = snsCommentService.findCommentListByPostId(snsPostId);

        return ResponseEntity.ok().body(snsCommentResponseDtoList);
    }
    @DeleteMapping("/snsComment/{snsCommentId}")
    @Operation(summary = "댓글 삭제", description = "SNS 게시글 댓글 삭제할 수 있는 API")
    public void deleteSnsComment(@PathVariable Long snsCommentId) {
        snsCommentService.deleteSnsComment(snsCommentId);
    }
}
