package com.example.plantpayservice.service;

import com.example.plantpayservice.domain.entity.CouponStatus;
import com.example.plantpayservice.domain.entity.OutboxEvent;
import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.exception.ErrorCode;
import com.example.plantpayservice.repository.IdempotencyKeyRepository;
import com.example.plantpayservice.repository.OutboxEventRepository;
import com.example.plantpayservice.repository.PaymentRepository;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import com.example.plantpayservice.vo.response.StatusResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper objectMapper;

    private static final int BETWEEN_ZERO_AND_ONE = 1;
    /**
     * 식구페이 머니 충전 메서드
     * iamport로 결제 완료 되면 페이 머니로 충전
     * 비즈니스 로직을 진행하고 멱등키 확인
     * @param : PaymentRequestDto paymentRequestDto, String key
     * @return StatusResponseDto
     */
    @Transactional
    public StatusResponseDto chargePayMoney(PaymentRequestDto paymentRequestDto, String key) {
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
        // 비즈니스 로직 수행 후 멱등성 키 저장
        isIdempotent(key);
        return StatusResponseDto.success();
    }
    /**
     * 식구페이 머니 환불 메서드
     * 원하는 금액 환불후 계좌 송금(실제로 계좌로 이체되진 않음)
     * 환불할 금액이 없을 경우 예외 처리
     * synchronized를 통해 동시성 제어!
     * @param : UpdatePaymentRequestDto paymentRequestDto
     */
    @Transactional
    public StatusResponseDto refundPayMoney(PaymentRequestDto paymentRequestDto, String key) {
        // memberNo로 보유 페이머니 조회
        Payment payment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());
        //보유 페이 머니보다 입력한 환불할 금액이 많으면 예외 처리
        if (payment.getPayMoney()- paymentRequestDto.getPayMoney() < 0) {
            throw ErrorCode.throwInsufficientRefundPayMoney();
        }
        payment.decreasePayMoney(paymentRequestDto.getPayMoney());
        paymentRepository.save(payment);
        // 비즈니스 로직 수행 후 멱등성 키 저장
        isIdempotent(key);
        return StatusResponseDto.success();
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
     * 식구페이로 거래 유효성 검증
     * 구매자가 보유한 금액보다 거래 금액이 더 크면 예외처리
     * 쿠폰 사용시 kafka를 통한 이벤트
     * 쿠폰 미적용시 바로 거래 결제 메서드 호출
     * @param : PaymentRequestDto paymentRequestDto
     */
    public StatusResponseDto validTransaction(PaymentRequestDto paymentRequestDto) {
        Payment buyerPayment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());

        //거레할 금액보다 구매자 보유 payMoney가 적으면 예외 처리
        if (buyerPayment.getPayMoney() < paymentRequestDto.getPayMoney()) {
            throw ErrorCode.throwInsufficientPayMoney();
        }
        StatusResponseDto statusResponseDto = StatusResponseDto.success();
        return statusResponseDto;
    }
    /**
     * 보상 트랜잭션 발생시키기 위한 임의 테스트 메서드
     */
    private void errorPerHalf() {
        int zeroOrOne = new Random().nextInt(BETWEEN_ZERO_AND_ONE);

        if (zeroOrOne == 0) {
            throw ErrorCode.paymentProcessingError();
        }
    }
    /**
     *
     * 식구페이 거래 메서드
     * 판매자 상대 멤버 번호를 통해 해당 조회 후
     * 판매자 paymoney += 거래할 금액
     * 구매자 Paymoney -= 거래할 금액
     * 분산 트랜잭션 Saga Pattern 적용
     * 쿠폰 사용 + 결제(사용자간의 거래) 워크플로우에 신뢰성을 보장하기 위해 Transactional outbox pattern + CDC 적용
     * 에러 발생시 쿠폰 마이크로서비스로 보상 트랜잭션 시작, Rollback
     * @param : PaymentRequestDto paymentRequestDto, Integer sellerNo
     */
    @Transactional
    public void tradePayMoney(PaymentRequestDto paymentRequestDto, String key) throws JsonProcessingException {
        OutboxEvent outboxEvent = parsingEvent(paymentRequestDto);
        isIdempotent(key);
        try {
            Payment buyerPayment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());
            Integer buyerPayMoney = paymentRequestDto.getPayMoney();

            if (paymentRequestDto.getCouponStatus() == CouponStatus.쿠폰사용) {
                //쿠폰 사용시 구매자 결제정보만 쿠폰금액 차감
                buyerPayMoney += paymentRequestDto.getDiscountPrice();
            }
//            errorPerHalf();
            paymentRepository.tradePayMoney(paymentRequestDto.getSellerNo(), buyerPayment.getMemberNo(), paymentRequestDto, buyerPayMoney);
        } catch (Exception e) {
            // 결제 실패 시 보상 트랜잭션 발행을 위한 outboxEvent
            log.error("===[결제 요청 오류] -> coupon-rollback ,  쿠폰 번호 :{} / {}====", paymentRequestDto.getCouponNo(), e.getMessage());
            outboxEventRepository.save(outboxEvent);
        }
    }

    private void isIdempotent(String key) {
        if (!idempotencyKeyRepository.addRequest(key)) {
            log.error("결제 관련 요청중 중복 감지: {}", key);
            throw ErrorCode.throwIdempotencyKeyExists();
        }
    }

    private OutboxEvent parsingEvent(PaymentRequestDto paymentRequestDto) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(paymentRequestDto);
        String couponNo = paymentRequestDto.getCouponNo() != null ? paymentRequestDto.getCouponNo().toString() : "쿠폰미사용";

        OutboxEvent outboxEvent = new OutboxEvent(couponNo,"Payment", "coupon-rollback", payload);
        return outboxEvent;
    }

}
