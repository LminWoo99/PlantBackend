package com.example.plantcouponservice.service.consumer;

import com.example.plantcouponservice.domain.OutboxEvent;
import com.example.plantcouponservice.service.CouponService;
import com.example.plantcouponservice.vo.request.PaymentRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponRollbackConsumer {
    private final CouponService couponService;
    private final ObjectMapper objectMapper;
    @KafkaListener(topics = "Payment.events", containerFactory = "paymentFailedConsumerFactory")
    public void handleCouponRollbackEvent(String eventPayload) throws JsonProcessingException {

        OutboxEvent outboxEvent = objectMapper.readValue(eventPayload, OutboxEvent.class);

        if ("coupon-rollback".equals(outboxEvent.getEventType())) {
            PaymentRequestDto paymentRequestDto = objectMapper.readValue(outboxEvent.getPayload(), PaymentRequestDto.class);
            log.info("======보상 트랜잭션 동작 , 쿠폰 번호 :{} =====", paymentRequestDto.getCouponNo());
            couponService.revertCouponStatus(paymentRequestDto);
        }
    }
}
