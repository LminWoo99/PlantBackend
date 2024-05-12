package com.example.plantpayservice.service;

import com.example.plantpayservice.domain.entity.CouponStatus;
import com.example.plantpayservice.exception.ErrorCode;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentSaga {
    private final KafkaTemplate<String, PaymentRequestDto> kafkaTemplate;
    private final PaymentService paymentService;
    // 쿠폰 사용 요청을 시작하는 메서드
    public void startSaga(PaymentRequestDto paymentRequestDto) {
        try {
            if (paymentRequestDto.getCouponStatus() == CouponStatus.쿠폰미사용) {
                kafkaTemplate.send("payment", paymentRequestDto);
            } else {
                kafkaTemplate.send("coupon-use", paymentRequestDto);
            }
        } catch (Exception e) {
            log.error("Failed to send Kafka message", e);
            ErrorCode.throwInsufficientPayMoney();
        }
    }


    @KafkaListener(topics = "payment", containerFactory = "paymentListenerContainerFactory")
    public void handlePayment(PaymentRequestDto paymentRequestDto) {
        log.info("결제 요청 시작 ==>");
        paymentService.tradePayMoney(paymentRequestDto);
    }
    @KafkaListener(topics = "payment-failed", containerFactory = "paymentFailedListenerContainerFactory")
    public void handlePaymentFailed(PaymentRequestDto paymentRequestDto) {
        log.info("결제 요청 오류 ==> 쿠폰 롤백");
        // 결제 실패 시 쿠폰 사용 취소 이벤트 발행
        kafkaTemplate.send("coupon-rollback", paymentRequestDto);
    }
}

