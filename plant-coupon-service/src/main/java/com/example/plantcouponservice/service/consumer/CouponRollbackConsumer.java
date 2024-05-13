package com.example.plantcouponservice.service.consumer;

import com.example.plantcouponservice.service.CouponService;
import com.example.plantcouponservice.vo.request.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponRollbackConsumer {
    private final CouponService couponService;
    @KafkaListener(topics = "coupon-rollback", containerFactory = "couponUseListenerContainerFactory")
    public void handleCouponRollbackEvent(PaymentRequestDto event) {
        log.info("======보상 트랜잭션 동작 , 쿠폰 번호 :{} =====" ,event.getCouponNo());
        couponService.revertCouponStatus(event);
    }
}
