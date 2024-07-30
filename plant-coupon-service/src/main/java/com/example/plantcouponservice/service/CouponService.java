package com.example.plantcouponservice.service;

import com.example.plantcouponservice.domain.OutboxEvent;
import com.example.plantcouponservice.repository.OutboxEventRepository;
import com.example.plantcouponservice.service.producer.CouponCreateProducer;
import com.example.plantcouponservice.service.producer.PaymentProducer;
import com.example.plantcouponservice.domain.Coupon;
import com.example.plantcouponservice.domain.CouponStatusEnum;
import com.example.plantcouponservice.repository.AppliedUserRepository;
import com.example.plantcouponservice.repository.CouponCountRepository;
import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.vo.request.CouponRequestDto;
import com.example.plantcouponservice.vo.request.CouponStatus;
import com.example.plantcouponservice.vo.request.PaymentRequestDto;
import com.example.plantcouponservice.vo.response.CouponResponseDto;
import com.example.plantcouponservice.vo.response.StatusResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;
    private final PaymentProducer paymentProducer;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * 쿠폰 발급
     * 요구사항 정리 : 하루에 중고 거래시 할인받을수 있는 100개 쿠폰을 발급
     * 쿠폰은 다음날 자정에 일괄 삭제
     * 100개 넘으면 발급 불가
     * @param : CouponRequestDto couponRequestDto
     */
    public StatusResponseDto applyCoupon(CouponRequestDto couponRequestDto) {
        // coupon 발급 전에 redis 싱글스레드 1증가
        Long add = appliedUserRepository.add(couponRequestDto.getMemberNo());
        if (add != 1) {
            return StatusResponseDto.addStatus(409);
        }
        Long count = couponCountRepository.increment();
        //오늘 날짜 기준으로 100개보다 많으면 return
        if (count > 100) {
            return StatusResponseDto.addStatus(429);
        }

        couponCreateProducer.create(couponRequestDto);
        return StatusResponseDto.success();
    }
    /**
     * 본인 쿠폰 조회
     * @param : Integer memberNo
     */
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getCoupon(Integer memberNo) {
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 및 시간
        LocalDateTime thirtyOneDaysBefore = now.minusDays(31); // 31일 전

        List<Coupon> couponList = couponRepository.findByRegDateBetween(memberNo, thirtyOneDaysBefore, now, CouponStatusEnum.사용가능);
        return couponList.stream().map(coupon -> CouponResponseDto.builder()
                .couponNo(coupon.getCouponNo())
                .memberNo(memberNo)
                .discountPrice(coupon.getDiscountPrice())
                .regDate(coupon.getRegDate())
                .build())
                .collect(Collectors.toList());
    }

    /**
     * 쿠폰 사용 메서드
     * 쿠폰 , 결제 마이크로서비스 saga pattern 적용
     * 쿠폰 사용 + 결제(사용자간의 거래) 워크플로우에 신뢰성을 보장하기 위해 Transactional outbox pattern + CDC 적용
     * coupon status가 미적용이면 바로 결제 이벤트
     * 적용이면 쿠폰 사용후 결제 이벤트
     * @param : PaymentRequestDto paymentRequestDto
     */
   @Transactional
    public void useCouponAndPayment(PaymentRequestDto paymentRequestDto) throws JsonProcessingException {
       //Transactional outbox pattern + CDC 적용
       OutboxEvent outboxEvent = parsingEvent(paymentRequestDto);

       if (paymentRequestDto.getCouponStatus()== CouponStatus.쿠폰미사용){
           log.info("======couponUseConsumer Data :{}======", paymentRequestDto);

           outboxEventRepository.save(outboxEvent);
           return;
       }
        Coupon coupon = couponRepository.findByMemberNoAndCouponNo(paymentRequestDto.getMemberNo(), paymentRequestDto.getCouponNo());
        //사용완료 및 변경감지
        coupon.useCoupon();
        log.info("======couponUseConsumer Data :{}======", paymentRequestDto);
        //쿠폰 적용완료후 결제 요청
        outboxEventRepository.save(outboxEvent);
   }
    /**
     * 쿠폰 롤백 메서드
     * 보상 트랜잭션
     * 결제 실패시 쿠폰 상태를 update
     * @param : PaymentRequestDto paymentRequestDto
     */
    @Transactional
    public void revertCouponStatus(PaymentRequestDto paymentRequestDto) {
        Coupon coupon = couponRepository.findByMemberNoAndCouponNo(paymentRequestDto.getMemberNo(), paymentRequestDto.getCouponNo());
        if (coupon.getType() == CouponStatusEnum.사용완료) {
            //다시 쿠폰 상태 롤백 및 변경감지
            coupon.revertCoupon();
        }
    }

    private OutboxEvent parsingEvent(PaymentRequestDto paymentRequestDto) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(paymentRequestDto);
        String couponNo = paymentRequestDto.getCouponNo().toString() != null ? paymentRequestDto.getCouponNo().toString() : "쿠폰미사용";

        OutboxEvent outboxEvent = new OutboxEvent(couponNo, "Coupon", "payment", payload);
        return outboxEvent;
    }
}
