package com.example.plantpayservice.service.event;

import com.example.plantpayservice.vo.request.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponRollbackProducer {
    private final KafkaTemplate<String, PaymentRequestDto> kafkaTemplate;
    public void rollbackCouponStatus(PaymentRequestDto couponRequestDto) {
        kafkaTemplate.send("coupon-rollback", couponRequestDto);
    }

}