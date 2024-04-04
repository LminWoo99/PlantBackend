package com.example.plantpayservice.client;

import com.example.plantpayservice.vo.response.CouponResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 마이크로서비스 간의 호출을 위한 feignclient
 *
 */
@FeignClient(name="plant-coupon-service")
public interface PlantCouponServiceClient {
    @PostMapping("/coupon/used")
    ResponseEntity<CouponResponseDto> useCoupon(@RequestParam Integer memberNo, @RequestParam Long couponNo);
}
