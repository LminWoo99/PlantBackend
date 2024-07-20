package com.example.plantpayservice.service.event;

import com.example.plantpayservice.domain.entity.OutboxEvent;
import com.example.plantpayservice.service.PaymentService;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestConsumer {
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "Coupon.events", containerFactory = "paymentListenerContainerFactory")
    public void handlePayment(String eventPayload) throws JsonProcessingException {
        OutboxEvent outboxEvent = objectMapper.readValue(eventPayload, OutboxEvent.class);

            PaymentRequestDto paymentRequestDto = objectMapper.readValue(outboxEvent.getPayload(), PaymentRequestDto.class);
            log.info("결제 요청 시작 ==> 결제 정보 : {}", paymentRequestDto);
            paymentService.tradePayMoney(paymentRequestDto);
    }
}

