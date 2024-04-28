package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.service.SnsHashTagMapService;
import com.example.plantsnsservice.vo.response.SnsHashResponseDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SnsHashTagMapController {
    private final SnsHashTagMapService snsHashTagMapService;

    @GetMapping("/sns-hash-tag/top-ten")
    @Operation(summary = "인기 해시태그 조회", description = "게시글에 사용 기준으로 인기 해시태그 조회")
    public ResponseEntity<List<SnsHashResponseDto>> findTop10SnsHashTag() {
        List<SnsHashResponseDto> top10SnsHashTag = snsHashTagMapService.findTop10SnsHashTag();
        return ResponseEntity.ok().body(top10SnsHashTag);
    }
}
