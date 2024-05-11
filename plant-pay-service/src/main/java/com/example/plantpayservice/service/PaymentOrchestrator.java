package com.example.plantpayservice.service;

import com.example.plantpayservice.domain.entity.CouponStatus;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentOrchestrator {
    private final KafkaTemplate<String, PaymentRequestDto> kafkaTemplate;
    private final PaymentService paymentService;

    // 쿠폰 사용 요청을 시작하는 메서드
    public void startSaga(PaymentRequestDto paymentRequestDto) {
        if (paymentRequestDto.getCouponStatus() == CouponStatus.쿠폰미사용){
            //유저가 쿠폰 미사용 ==> 바로 결제 이벤트
            kafkaTemplate.send("payment", paymentRequestDto);
        }
        else {
            kafkaTemplate.send("coupon-use", paymentRequestDto);
        }
    }

    @KafkaListener(topics = "coupon-success")
    public void handleCouponUsed(PaymentRequestDto paymentRequestDto) {
        log.info("쿠폰 사용 완료후 결제 요청 ==>");
        // 쿠폰 사용 성공 시 결제 요청 이벤트 발행
        kafkaTemplate.send("payment", paymentRequestDto);
    }

    @KafkaListener(topics = "payment")
    public void handlePayment(PaymentRequestDto paymentRequestDto) {
        log.info("결제 요청 시작 ==>");
        paymentService.tradePayMoney(paymentRequestDto);
    }
    @KafkaListener(topics = "payment-failed")
    public void handlePaymentFailed(PaymentRequestDto paymentRequestDto) {
        log.info("결제 요청 오류 ==> 쿠폰 롤백");
        // 결제 실패 시 쿠폰 사용 취소 이벤트 발행
        kafkaTemplate.send("coupon-rollback", paymentRequestDto);
    }
}

