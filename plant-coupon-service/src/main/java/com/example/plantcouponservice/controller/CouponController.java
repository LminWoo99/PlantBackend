package com.example.plantcouponservice.controller;

import com.example.plantcouponservice.service.CouponService;
import com.example.plantcouponservice.vo.CouponRequestDto;
import com.example.plantcouponservice.vo.CouponResponseDto;
import com.example.plantcouponservice.vo.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    @PostMapping("/coupon")
    public ResponseEntity<?> applyCoupon(@RequestBody CouponRequestDto couponRequestDto) {
        try {
            StatusResponseDto statusResponseDto = couponService.applyCoupon(couponRequestDto);
            if (statusResponseDto.getStatus() == 409) {
                return ResponseEntity.status(409).body("한 계정당 쿠폰은 하나만 발급 가능합니다.");
            }
            if (statusResponseDto.getStatus() == 429) {
                return ResponseEntity.status(429).body("오늘의 쿠폰은 모두 소진되었습니다.");
            }
            return ResponseEntity.ok().body("쿠폰이 성공적으로 발급되었습니다.");

        } catch (Exception e) {
            // 기타 서버 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 처리 중 오류가 발생했습니다.");
        }
    }
    //본인 쿠폰 조회
    @GetMapping("/coupon/{memberNo}")
    public ResponseEntity<List<CouponResponseDto>> getCoupon(@PathVariable Integer memberNo) {
        List<CouponResponseDto> couponResponseDtoList = couponService.getCoupon(memberNo);
        return ResponseEntity.ok().body(couponResponseDtoList);
    }

    //쿠폰 사용
    @PostMapping("/coupon/used")
    public ResponseEntity<CouponResponseDto> useCoupon(@RequestParam Integer memberNo, @RequestParam Long couponNo) {
        CouponResponseDto couponResponseDto = couponService.useCoupon(memberNo, couponNo);
        return ResponseEntity.ok().body(couponResponseDto);

    }




}
