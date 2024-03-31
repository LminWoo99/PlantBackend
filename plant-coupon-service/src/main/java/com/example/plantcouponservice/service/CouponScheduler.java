package com.example.plantcouponservice.service;

import com.example.plantcouponservice.repository.AppliedUserRepository;
import com.example.plantcouponservice.repository.CouponCountRepository;
import com.example.plantcouponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponScheduler {

    private final CouponRepository couponRepository;
    private final AppliedUserRepository appliedUserRepository;
    private final CouponCountRepository couponCountRepository;



    // 매일 자정에 쿠폰 삭제
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAllCoupons() {
        couponRepository.deleteAll();
        log.info("모든 쿠폰이 삭제되었습니다. 삭제된 시각: {}", LocalDateTime.now());

        // Redis에서 적용된 사용자 데이터 삭제
        appliedUserRepository.deleteAllAppliedUsers();
        log.info("사용자 데이터가 Redis에서 삭제되었습니다");

        // Redis에서 쿠폰 카운트 데이터 리셋
        Long resetCouponCount = couponCountRepository.resetCouponCount();
        log.info("쿠폰 카운트가 Redis에서 {}로 리셋되었습니다", resetCouponCount);

    }
}