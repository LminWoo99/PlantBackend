package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.service.SnsPostService;
import com.example.plantsnsservice.service.SnsPostServiceFacade;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SnsPostController {
    private final SnsPostService snsPostService;
    private final SnsPostServiceFacade snsPostServiceFacade;

    @PostMapping("/snsPosts")
    @Operation(summary = "SNS 게시글 작성", description = "게시글 작성할 수 있는 API")
    public ResponseEntity<Long> createSnsPost(@RequestPart SnsPostRequestDto snsPostRequestDto, @RequestPart("file") List<MultipartFile> files) throws IOException {
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


    @GetMapping("snsPosts/search")
    @Operation(summary = "SNS 게시글 동적 검색", description = "해시태그, 글 본문, 글 제목, 닉네임을 이용한 검색 가능한 API")
    public ResponseEntity<List<SnsPostResponseDto>> getSnsPostListByCondition(@RequestParam Map<String, String> searchCondition) {
        List<SnsPostResponseDto> snsPostListByCondition = snsPostService.getSnsPostListByCondition(searchCondition);
        return ResponseEntity.ok().body(snsPostListByCondition);
    }

    @GetMapping("/snsPosts/nickname/{createdBy}")
    @Operation(summary = "SNS 게시글 닉네임으로 게시글 조회", description = "닉네임으로 자기 프로필에서 게시글 조회할 수 있는 API")
    public ResponseEntity<List<SnsPostResponseDto>> getSnsPostByCreatedBy(@PathVariable String createdBy) {
        List<SnsPostResponseDto> snsPostByCreated = snsPostService.getSnsPostByCreated(createdBy);
        return ResponseEntity.ok().body(snsPostByCreated);
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

    @PostMapping("/snsPost/likes/")
    @Operation(summary = "SNS 게시글 좋아요", description = "SNS 게시글 좋아요 기능 API")
    public ResponseEntity<HttpStatus> updateSnsLikesCount(@RequestParam Long id, @RequestParam Integer memberNo) {
        //lock 범위를 트랜잭션 범위보다 크게 잡기위해 퍼사드 사용
        snsPostServiceFacade.updateSnsLikesCountLock(id, memberNo);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/snsPost/week/top-ten")
    @Operation(summary = "이주의 인기 게시글 조회", description = "조회수 기준으로 1주일 사이 인기 게시글 조회")
    public ResponseEntity<List<SnsPostResponseDto>> findTop10PostsByWeek() {
        List<SnsPostResponseDto> top10PostsByWeek = snsPostService.findTop10PostsByWeek();
        return ResponseEntity.ok().body(top10PostsByWeek);
    }
    @GetMapping("/snsPost/month/top-twenty")
    @Operation(summary = "이달의 인기 게시글 조회", description = "조회수 기준으로 1달 사이 인기 게시글 조회")
    public ResponseEntity<List<SnsPostResponseDto>> findTop20PostsByMonth() {
        List<SnsPostResponseDto> top20PostsByMonth = snsPostService.findTop20PostsByMonth();
        return ResponseEntity.ok().body(top20PostsByMonth);
    }
}
