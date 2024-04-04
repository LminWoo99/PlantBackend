package com.example.plantpayservice.service;

import com.example.plantpayservice.client.PlantCouponServiceClient;
import com.example.plantpayservice.domain.entity.CouponStatus;
import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.exception.CustomException;
import com.example.plantpayservice.exception.ErrorCode;
import com.example.plantpayservice.repository.PaymentRepository;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.CouponResponseDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final PlantCouponServiceClient plantCouponServiceClient;
    /**
     * 식구페이 머니 충전 메서드
     * iamport로 결제 완료 되면 페이 머니로 충전
     * @param : PaymentRequestDto paymentRequestDto
     */
    @Transactional
    public void chargePayMoney(PaymentRequestDto paymentRequestDto) {
        if (!paymentRepository.existsByMemberNo(paymentRequestDto.getMemberNo())) {
            Payment payment=Payment.builder()
                    .payMoney(paymentRequestDto.getPayMoney())
                    .memberNo(paymentRequestDto.getMemberNo())
                    .build();

            paymentRepository.save(payment);
        }
        else{
            paymentRepository.existsByMemberNoUpdatePayMoney(paymentRequestDto);

        }

    }
    /**
     * 식구페이 머니 환불 메서드
     * 원하는 금액 환불후 계좌 송금(실제로 계좌로 이체되진 않음)
     * 환불할 금액이 없을 경우 예외 처리
     * 사용자가 모르고 환불요청을 두번 이상 연속 했을 경우를 대비해(적절한 ui/ux설계가 없으므로)
     * synchronized를 통해 동시성 제어!
     * @param : UpdatePaymentRequestDto paymentRequestDto
     */
    public synchronized void refundPayMoney(PaymentRequestDto paymentRequestDto) {
        // memberNo로 보유 페이머니 조회
        Payment payment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());
        //보유 페이 머니보다 입력한 환불할 금액이 많으면 예외 처리
        if (payment.getPayMoney()- paymentRequestDto.getPayMoney() < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PAYMONEY);
        }
        payment.decreasePayMoney(paymentRequestDto.getPayMoney());
        paymentRepository.saveAndFlush(payment);
    }
    /**
     * 식구페이 머니 조회 메서드
     * 조회용 메서드라 @Transactional(readOnly = true) 처리
     * @param : Integer memberNo
     */
    @Transactional(readOnly = true)
    public PaymentResponseDto getPayMoney(Integer memberNo) {
        Payment payment = paymentRepository.findByMemberNo(memberNo);
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .payMoney(payment.getPayMoney())
                .memberNo(payment.getMemberNo())
                .build();
        return paymentResponseDto;

    }
    /**
     *
     * 식구페이 거래 메서드
     * 판매자 상대 멤버 번호를 통해 해당 조회 후
     * 판매자 paymoney += 거래할 금액
     * 구매자 Paymoney -= 거래할 금액
     * @param : PaymentRequestDto paymentRequestDto, Integer sellerNo
     */

    @Transactional
    public void tradePayMoney(PaymentRequestDto paymentRequestDto, Integer sellerNo) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        Payment buyerPayment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());
        Integer buyerPayMoney = paymentRequestDto.getPayMoney();
        //쿠폰 사용시 구매자 결제정보만 3000원 차감
        if (paymentRequestDto.getCouponStatus() == CouponStatus.쿠폰사용) {
            //쿠폰 마이크로 서비스 호출및 circuitBreaker 예외
           circuitBreaker.run(() ->
                            plantCouponServiceClient.useCoupon(buyerPayment.getMemberNo(), paymentRequestDto.getCouponNo()),
                    throwable -> ResponseEntity.ok(null));
            buyerPayMoney += paymentRequestDto.getDiscountPrice();
        }
        //거레할 금액보다 구매자 보유 payMoney가 적으면 예외 처리
        if (buyerPayment.getPayMoney()< paymentRequestDto.getPayMoney()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PAYMONEY);
        }
        paymentRepository.tradePayMoney(sellerNo, buyerPayment.getMemberNo(), paymentRequestDto, buyerPayMoney);
    }
}
