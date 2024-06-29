package com.example.plantcouponservice.service;

import com.example.plantcouponservice.domain.CouponStatusEnum;
import com.example.plantcouponservice.domain.FailedEvent;
import com.example.plantcouponservice.repository.AppliedUserRepository;
import com.example.plantcouponservice.repository.CouponCountRepository;
import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.repository.FailedEventRepository;
import com.example.plantcouponservice.service.producer.CouponCreateProducer;
import com.example.plantcouponservice.vo.request.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponScheduler {

    private final CouponRepository couponRepository;
    private final AppliedUserRepository appliedUserRepository;
    private final CouponCountRepository couponCountRepository;
    private final FailedEventRepository failedEventRepository;
    private final CouponCreateProducer couponCreateProducer;


    // 매일 자정에 쿠폰 삭제
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void deleteAllCoupons() {
        // Redis에서 적용된 사용자 데이터 삭제
        appliedUserRepository.deleteAllAppliedUsers();
        log.info("사용자 데이터가 Redis에서 삭제되었습니다");

        // Redis에서 쿠폰 카운트 데이터 리셋
        Long resetCouponCount = couponCountRepository.resetCouponCount();
        log.info("쿠폰 카운트가 Redis에서 {}로 리셋되었습니다", resetCouponCount);


        couponRepository.deleteByType(CouponStatusEnum.사용완료);
        log.info("모든 쿠폰이 삭제되었습니다. 삭제된 시각: {}", LocalDateTime.now());

    }
    // FailedEvent Coupon 재발급
    @Scheduled(cron = "0 30 13-23 * * *", zone = "Asia/Seoul")
    public void getCouponFailedEvent() {
        List<FailedEvent> failedEvents = failedEventRepository.findAll();
        for (FailedEvent failedEvent : failedEvents) {
            log.info("쿠폰이 재발급됩니다. {}", failedEvent.getId());
            CouponRequestDto couponRequestDto = new CouponRequestDto(failedEvent.getMemberNo(), failedEvent.getDiscountPrice());
            couponCreateProducer.create(couponRequestDto);
        }
    }
}