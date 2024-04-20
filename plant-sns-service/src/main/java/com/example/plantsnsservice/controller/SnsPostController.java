package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.service.SnsPostService;
import com.example.plantsnsservice.vo.request.ImageRequestDto;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SnsPostController {
    private final SnsPostService snsPostService;
    @PostMapping("/snsPosts")
    @Operation(summary = "SNS 게시글 작성", description = "게시글 작성할 수 있는 API")
    public ResponseEntity<Long> createSnsPost(@RequestPart SnsPostRequestDto snsPostRequestDto, @RequestPart ("file")List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(snsPostService.createPost(snsPostRequestDto, files));
    }
    @GetMapping("/snsPosts")
    @Operation(summary = "SNS 게시글 전체 조회", description = "전체 게시글 조회할 수 있는 API")
    public ResponseEntity<List<SnsPostResponseDto>> getSnsPostList() {
        List<SnsPostResponseDto> snsPostList = snsPostService.getSnsPostList();
        return ResponseEntity.ok().body(snsPostList);
    }
    @GetMapping("/snsPost/{snsPostId}")
    @Operation(summary = "SNS 게시글 단건 조회", description = "하나의 게시글 조회할 수 있는 API")
    public ResponseEntity<SnsPostResponseDto> getSnsPost(@PathVariable Long snsPostId) {
        SnsPostResponseDto snsPostResponseDto = snsPostService.findById(snsPostId);
        return ResponseEntity.ok().body(snsPostResponseDto);
    }
    @GetMapping("/snsPosts/nickname/{createdBy}")
    @Operation(summary = "SNS 게시글 닉네임으로 게시글 조회", description = "닉네임으로 자기 프로필에서 게시글 조회할 수 있는 API")
    public ResponseEntity<List<SnsPostResponseDto>> getSnsPostByCreatedBy(@PathVariable String createdBy) {
        List<SnsPostResponseDto> snsPostByCreated = snsPostService.getSnsPostByCreated(createdBy);
        return ResponseEntity.ok().body(snsPostByCreated);
    }
    @GetMapping("/snsPosts/{hashTagName}")
    @Operation(summary = "SNS 게시글 해시 태그 기준으로 조회", description = "해시 태그 기준으로 게시글 조회할 수 있는 API")
    public ResponseEntity<List<SnsPostResponseDto>> getSnsPostsByHashTag(@PathVariable String hashTagName) {
        List<SnsPostResponseDto> snsPostResponseDtoList = snsPostService.findAllByHashTag(hashTagName);
        return ResponseEntity.ok().body(snsPostResponseDtoList);

    }
    @PatchMapping("/snsPost")
    @Operation(summary = "SNS 게시글 수정", description = "SNS 게시글 수정할 수 있는 API")
    public ResponseEntity<HttpStatus> updateSnsPost(@RequestBody SnsPostRequestDto snsPostRequestDto) {
        snsPostService.updateSnsPost(snsPostRequestDto);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/snsPost/{id}")
    @Operation(summary = "SNS 게시글 삭제", description = "SNS 게시글 삭제할 수 있는 API")
    public ResponseEntity<HttpStatus> deleteSnsPost(@PathVariable Long id) {
        snsPostService.deleteSnsPost(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}
