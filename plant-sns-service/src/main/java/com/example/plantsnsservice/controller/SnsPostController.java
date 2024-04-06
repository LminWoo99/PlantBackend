package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.repository.SnsPostRepository;
import com.example.plantsnsservice.service.SnsPostService;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SnsPostController {
    private final SnsPostService snsPostService;
    //게시글 작성
    @PostMapping("/snsPost")
    public ResponseEntity<HttpStatus> createSnsPost(@RequestBody SnsPostRequestDto snsPostRequestDto) {
        snsPostService.createPost(snsPostRequestDto);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
    @GetMapping
    public ResponseEntity<List<SnsPostResponseDto>> getSnsPostList() {
        List<SnsPostResponseDto> snsPostList = snsPostService.getSnsPostList();
        return ResponseEntity.ok().body(snsPostList);
    }

    @PatchMapping("/snsPost")
    public ResponseEntity<HttpStatus> updateSnsPost(@RequestBody SnsPostRequestDto snsPostRequestDto) {
        snsPostService.updateSnsPost(snsPostRequestDto);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/snsPost/{id}")
    public ResponseEntity<HttpStatus> deleteSnsPost(@PathVariable Long id) {
        snsPostService.deleteSnsPost(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }




}
