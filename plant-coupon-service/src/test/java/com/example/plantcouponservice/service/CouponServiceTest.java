package com.example.plantcouponservice.service;

import com.example.plantcouponservice.domain.Coupon;
import com.example.plantcouponservice.domain.CouponStatusEnum;
import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.vo.CouponRequestDto;
import com.example.plantcouponservice.vo.CouponResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CouponServiceTest {
    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Test
    @DisplayName("여러번응모")
    void applyCouponTest() throws InterruptedException {
        //given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i<threadCount; i++) {
            CouponRequestDto couponRequestDto = new CouponRequestDto(i, 1L, 2000);
            executorService.submit(() -> {
                try{
                    couponService.applyCoupon(couponRequestDto);
                }
                finally {
                    countDownLatch.countDown();

                }
            });
        }
        countDownLatch.await();
        Thread.sleep(10000);
        long count = couponRepository.count();
        assertThat(count).isEqualTo(100);
    }
    @Test
    @DisplayName("한명당 쿠폰 발급 한개 테스트")
    void applyCouponByOneTest() throws Exception{
        //given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i<threadCount; i++) {
            CouponRequestDto couponRequestDto = new CouponRequestDto(1, 1L, 2000);
            executorService.submit(() -> {
                try{
                    couponService.applyCoupon(couponRequestDto);
                }
                finally {
                    countDownLatch.countDown();

                }
            });
        }
        countDownLatch.await();

        long count = couponRepository.count();
        assertThat(count).isEqualTo(1);


        //then

    }
    @Test
    @DisplayName("쿠폰조회 테스트")
    void getCouponTest() {
        //given
        CouponRequestDto couponRequestDto = new CouponRequestDto(3, 1L, 2000);
        couponService.applyCoupon(couponRequestDto);

        //when
        CouponResponseDto couponResponseDto = couponService.getCoupon(3);
        //then
        Assertions.assertThat(couponResponseDto.getDiscountPrice()).isEqualTo(2000);


    }

    @Test
    @DisplayName("쿠폰 사용 테스트")
    void useCouponTest() {
        //given
        CouponRequestDto couponRequestDto = new CouponRequestDto(4, 1L, 2000);
        couponService.applyCoupon(couponRequestDto);
        //when
        couponService.useCoupon(3);
        Coupon coupon = couponRepository.findByMemberNo(3);
        //then

        assertThat(coupon.getType()).isEqualTo(CouponStatusEnum.사용완료);


    }
}