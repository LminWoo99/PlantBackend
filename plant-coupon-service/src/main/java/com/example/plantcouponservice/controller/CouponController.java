package com.example.plantcouponservice.controller;

import com.example.plantcouponservice.service.CouponService;
import com.example.plantcouponservice.vo.request.CouponRequestDto;
import com.example.plantcouponservice.vo.request.PaymentRequestDto;
import com.example.plantcouponservice.vo.response.CouponResponseDto;
import com.example.plantcouponservice.vo.response.StatusResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "쿠폰 발급", description = "쿠폰 발급할 수 있는 API, 발급시 한 계정당 쿠폰 하나 및 선착순 100명만 발급 가능")
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
    @GetMapping("/coupon/{memberNo}")
    @Operation(summary = "본인 쿠폰 조회", description = "본인 쿠폰 조회할 수 있는 API")
    public ResponseEntity<List<CouponResponseDto>> getCoupon(@PathVariable Integer memberNo) {
        List<CouponResponseDto> couponResponseDtoList = couponService.getCoupon(memberNo);
        return ResponseEntity.ok().body(couponResponseDtoList);
    }
    @PostMapping("/coupon/payment")
    @Operation(summary = "쿠폰 사용하여 결제", description = "쿠폰 사용할 수 있는 API")
    public void useCoupon(@RequestBody PaymentRequestDto paymentRequestDto) throws JsonProcessingException {
        couponService.useCouponAndPayment(paymentRequestDto);
    }
}
