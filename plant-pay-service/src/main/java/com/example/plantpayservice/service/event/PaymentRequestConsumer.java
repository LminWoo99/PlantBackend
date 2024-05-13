package com.example.plantpayservice.service.event;

import com.example.plantpayservice.service.PaymentService;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "payment", containerFactory = "paymentListenerContainerFactory")
    public void handlePayment(PaymentRequestDto paymentRequestDto) {
        log.info("결제 요청 시작 ==> 결제 정보 : {}", paymentRequestDto);
        paymentService.tradePayMoney(paymentRequestDto);
    }
}

