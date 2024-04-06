package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.service.SnsCommentService;
import com.example.plantsnsservice.vo.request.SnsCommentRequestDto;
import com.example.plantsnsservice.vo.response.SnsCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SnsCommentController {
    private final SnsCommentService snsCommentService;

    //댓글 저장
    @PostMapping("/snsComment")
    public ResponseEntity<SnsCommentResponseDto> createSnsComment(@RequestBody SnsCommentRequestDto snsCommentRequestDto) {
        SnsCommentResponseDto snsCommentResponseDto = snsCommentService.createComment(snsCommentRequestDto);
        return ResponseEntity.ok().body(snsCommentResponseDto);
    }

    // 댓글 , 대댓글 조회
    @GetMapping("/snsComment/{snsPostId}")
    public ResponseEntity<List<SnsCommentResponseDto>> getSnsComment(@PathVariable Long snsPostId) {
        List<SnsCommentResponseDto> snsCommentResponseDtoList = snsCommentService.findCommentListByPostId(snsPostId);

        return ResponseEntity.ok().body(snsCommentResponseDtoList);
    }

    // 댓글 삭제
    @DeleteMapping("/snsComment/{snsCommentId}")
    public void deleteSnsComment(@PathVariable Long snsCommentId) {
        snsCommentService.deleteSnsComment(snsCommentId);
    }
}
